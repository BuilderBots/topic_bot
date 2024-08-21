package com.topic.bots.handlers.topic;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.topic.bots.common.Commands;
import com.topic.bots.common.CommonCache;
import com.topic.bots.database.entity.BannedUser;
import com.topic.bots.database.entity.User;
import com.topic.bots.database.enums.BannedStatus;
import com.topic.bots.database.service.BannedService;
import com.topic.bots.database.service.UserService;
import com.topic.bots.handlers.AbstractHandler;
import com.topic.bots.helper.MessageHelper;
import com.topic.bots.helper.StrHelper;
import com.topic.bots.listener.AsyncBotApiMethod;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.forum.DeleteForumTopic;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *     处理话题消息
 * </p>
 *
 * @author zyred
 * @since v 0.0.1
 */
@Component
public class TopicHandler extends AbstractHandler {

    @Resource private UserService userService;
    @Resource private BannedService bannedService;

    @Override
    protected boolean support(Update update) {
        return update.hasMessage()
                && update.getMessage().isTopicMessage();
    }

    @Override
    protected BotApiMethod<?> execute(Update update) {
        Message message = update.getMessage();
        Long userId = CommonCache.getUserId(message.getMessageThreadId());

        if (StrUtil.equalsAny(message.getText(), "结束服务", "终止服务", "服务结束")) {
            AsyncBotApiMethod.addMessage(
                    DeleteForumTopic.builder()
                            .chatId(message.getChatId())
                            .messageThreadId(message.getMessageThreadId())
                            .build()
            );

            // 开启了复用
            if (Boolean.FALSE.equals(CommonCache.getSystem().getTopicReuse())) {
                this.userService.update(
                        Wrappers.<User>lambdaUpdate()
                                .set(User::getTopicId, null)
                                .eq(User::getUserId, userId)
                );
            }

            CommonCache.removeTopic(userId);
            return SendMessage.builder()
                    .chatId(userId)
                    .parseMode(ParseMode.MARKDOWNV2)
                    .text(StrHelper.comboMessage(CommonCache.getSystem().getFinishText()))
                    .disableWebPagePreview(true)
                    .build();
        }

        if (StrUtil.startWithAny(message.getText(), "封禁用户", "解封用户")) {
            boolean banned = StrUtil.startWith(message.getText(), "封禁用户");
            List<String> commands = StrUtil.split(message.getText(), Commands.WILL);

            BannedUser bannedUser = this.bannedService.getBannedUser(userId);
            if (banned) {
                if (Objects.isNull(bannedUser)) {
                    BannedUser save = new BannedUser(
                            userId,
                            commands.size() >= 2 ? commands.get(1) : null
                    );
                    this.bannedService.save(save);
                }
                CommonCache.banned(userId);
            } else {
                this.bannedService.update(
                        Wrappers.<BannedUser>lambdaUpdate()
                                .set(BannedUser::getBannedStatus, BannedStatus.UNBANNED)
                                .eq(BannedUser::getUserId, userId)
                );
                CommonCache.unbanned(userId);
            }
            SendMessage ok = ok(message);
            ok.setMessageThreadId(message.getMessageThreadId());
            return ok;
        }

        // 正常消息的发送
        String text = StrHelper.comboMessage(message.getText());
        return MessageHelper.commonAsync(message, userId, null, text, ParseMode.MARKDOWNV2);
    }
}
