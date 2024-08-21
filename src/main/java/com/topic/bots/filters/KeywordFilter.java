package com.topic.bots.filters;

import cn.hutool.core.util.StrUtil;
import com.topic.bots.common.CommonCache;
import com.topic.bots.helper.StrHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * <p>
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Component
public class KeywordFilter extends AbstractFilter {
    @Override
    public BotApiMethod<?> filter(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return null;
        }

        Message message = update.getMessage();

        String content = CommonCache.getKeywords(message.getText());
        if (StrUtil.isNotBlank(content)) {
            // 正常消息的发送
            String text = StrHelper.comboMessage(content);
            return okMarkdown(message, text);
        }

        return null;
    }
}
