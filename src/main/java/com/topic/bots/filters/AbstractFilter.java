package com.topic.bots.filters;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public abstract class AbstractFilter {

    private static final List<AbstractFilter> FILTERS = new ArrayList<>();

    public abstract BotApiMethod<?> filter (Update update);

    public AbstractFilter() {
        FILTERS.add(this);
    }

    public static BotApiMethod<?> doFilter (Update update) {
        for (AbstractFilter filter : FILTERS) {
            BotApiMethod<?> msg = filter.filter(update);
            if (Objects.nonNull(msg)) {
               return msg;
            }
        }
        return null;
    }

    protected SendMessage okMarkdown(Message message, String text) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .parseMode(ParseMode.MARKDOWNV2)
                .disableWebPagePreview(true)
                .text(text)
                .build();
    }

    protected SendMessage fail (Message message, String text) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(text)
                .build();
    }
}
