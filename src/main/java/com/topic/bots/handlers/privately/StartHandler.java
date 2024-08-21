package com.topic.bots.handlers.privately;

import cn.hutool.core.util.StrUtil;
import com.topic.bots.common.CommonCache;
import com.topic.bots.common.KeyboardCommon;
import com.topic.bots.database.entity.Sys;
import com.topic.bots.database.entity.User;
import com.topic.bots.database.service.UserService;
import com.topic.bots.handlers.AbstractHandler;
import com.topic.bots.helper.StrHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.forum.CreateForumTopic;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.forum.ForumTopic;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 *      start 命令处理器
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Slf4j
@Component
public class StartHandler extends AbstractHandler {

    @Resource private UserService userService;
    @Resource private TelegramClient telegramClient;

    @Override
    protected boolean support(Update update) {
        return update.hasMessage() && StrUtil.equals(update.getMessage().getText(), "/start");
    }

    @Override
    protected BotApiMethod<?> execute(Update update) {
        Message message = update.getMessage();
        Sys system = CommonCache.getSystem();

        User old = this.userService.selectUserId(message.getChatId());
        if (Objects.isNull(old)) {
            old = new User()
                    .setUserId(message.getChatId())
                    .setUsername(message.getChat().getUserName())
                    .setNickname(StrHelper.nickname(
                            message.getChat().getFirstName(),
                            message.getChat().getLastName()
                    ));
            this.userService.save(old);
        }

        // 创建一个 topic 话题组
        Long forwardId = system.getForwardId();
        if (Objects.isNull(forwardId)) {
            return this.warnSettingForwardId(system);
        }

        try {
            if (Objects.isNull(CommonCache.getTopicId(old.getUserId()))) {
                if (Boolean.TRUE.equals(CommonCache.getSystem().getTopicReuse())) {
                    // 新用户
                    if (Objects.isNull(old.getTopicId())) {
                        this.doSyncCreateUserTopic(old, forwardId);
                    } else {
                        CommonCache.setUserTopicId(old.getUserId(), old.getTopicId());
                    }
                } else {
                    if (Objects.nonNull(old.getTopicId())) {
                        CommonCache.setUserTopicId(old.getUserId(), old.getTopicId());
                    } else {
                        this.doSyncCreateUserTopic(old, forwardId);
                    }
                }
            }
        } catch (TelegramApiException ex) {
            log.error("创建topic错误，错误信息：{}", ex.getMessage(), ex);
            return this.fail(message, "客服繁忙，请稍后重试");
        }

        ReplyKeyboardMarkup keyboard = KeyboardCommon.parseJson(system.getReplyKeyboard());
        return SendMessage.builder()
                .text(StrHelper.replace(system.getGreetings(), old.getUsername(), old.getNickname(), null))
                .chatId(message.getChatId())
                .replyMarkup(keyboard)
                .parseMode(ParseMode.MARKDOWN)
                .build();
    }

    private void doSyncCreateUserTopic(User old, Long forwardId) throws TelegramApiException {
        Sys system = CommonCache.getSystem();
        String topicName = StrHelper.replace(system.getTopicName(), null, old.getNickname(), old.getUserId());
        ForumTopic topic = this.telegramClient.execute(CreateForumTopic.builder()
                .chatId(forwardId)
                .iconColor(0x6FB9F0)
                .name(topicName)
                .build());

        User user = new User();
        user.setUserId(old.getUserId());
        user.setTopicId(topic.getMessageThreadId());
        this.userService.updateAny(user);
        CommonCache.setUserTopicId(old.getUserId(), topic.getMessageThreadId());
    }

    private BotApiMethod<?> warnSettingForwardId(Sys system) {
        return SendMessage.builder()
                .text("系统未配置客服群，请在后台群内配置客服群ID，命令：设置#客服群#群ID")
                .chatId(system.getBackgroundId())
                .build();
    }
}
