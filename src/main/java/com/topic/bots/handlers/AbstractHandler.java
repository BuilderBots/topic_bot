package com.topic.bots.handlers;

import cn.hutool.json.JSONUtil;
import com.topic.bots.common.CommonCache;
import com.topic.bots.config.Constants;
import com.topic.bots.filters.AbstractFilter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Slf4j
public abstract class AbstractHandler {

    protected static final Set<Long> BOT_MANAGES = new HashSet<>(128);
    private static final List<AbstractHandler> HANDLERS = new ArrayList<>(128);


    protected AbstractHandler() {
        HANDLERS.add(this);
    }

    private static List<AbstractHandler> getHandlers () {
        return HANDLERS;
    }

    public static BotApiMethod<?> doExecute(Update update) {

        BotApiMethod<?> message = AbstractFilter.doFilter(update);
        if (Objects.nonNull(message)) {
            if (message == CommonCache.NULL_MESSAGE) {
                return null;
            }
            return message;
        }

        for (AbstractHandler handler : getHandlers()) {
            if (handler.support(update)) {
                BotApiMethod<?> result = handler.execute(update);
                if (Objects.nonNull(result)) {
                    log.info("处理器：{}，\n处理消息内容：{}\n响应结果：{}",
                            handler.getClass().getSimpleName(),
                            JSONUtil.toJsonStr(update),
                            JSONUtil.toJsonStr(result)
                    );
                }
                return result;
            }
        }
        return null;
    }

    protected abstract boolean support(Update update);

    protected abstract BotApiMethod<?> execute(Update update);

    protected boolean fromGroupMessage(Message message) {
        return message.isGroupMessage() || message.isSuperGroupMessage();
    }

    protected boolean fromBackground(Message message, Long backgroundId) {
        return Objects.equals(message.getChatId(), backgroundId);
    }

    protected boolean fromBotMange(Long userId) {
        return BOT_MANAGES.contains(userId);
    }


    protected SendMessage ok(Message message) {
        return this.ok(message, Constants.SUCCESS);
    }

    protected SendMessage okReply(Message message) {
        SendMessage ok = this.ok(message, Constants.SUCCESS);
        ok.setReplyToMessageId(message.getMessageId());
        return ok;
    }

    protected SendMessage okMarkdown(Message message, String text) {
        SendMessage ok = this.ok(message, text);
        ok.setReplyToMessageId(message.getMessageId());
        ok.setParseMode(ParseMode.MARKDOWNV2);
        ok.setDisableWebPagePreview(true);
        return ok;
    }

    protected SendMessage ok(Message message, String text) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(text)
                .build();
    }

    protected SendMessage fail (Message message) {
        return this.fail(message, Constants.FAILED);
    }

    protected SendMessage fail (Message message, String text) {
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(message.getChatId())
                .text(text)
                .build();
    }
}
