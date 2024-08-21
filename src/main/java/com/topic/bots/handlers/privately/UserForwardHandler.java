package com.topic.bots.handlers.privately;

import com.topic.bots.common.CommonCache;
import com.topic.bots.database.entity.Sys;
import com.topic.bots.handlers.AbstractHandler;
import com.topic.bots.helper.MessageHelper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Objects;

/**
 * <p>
 * </p>
 *
 * @author zyred
 * @since v 0.0.1
 */
@Component
@DependsOn("startHandler")
public class UserForwardHandler extends AbstractHandler {

    @Override
    protected boolean support(Update update) {
        return update.hasMessage()
                && update.getMessage().isUserMessage();
    }

    @Override
    protected BotApiMethod<?> execute(Update update) {
        Message message = update.getMessage();
        Sys system = CommonCache.getSystem();

        // 用户自己的 topicId
        Integer topicId = CommonCache.getTopicId(message.getChat().getId());
        if (Objects.isNull(topicId)) {
            return fail(message, "请发送 /start 命令开始使用！");
        }
        return MessageHelper.commonAsync(message, system.getForwardId(), topicId, null, null);
    }
}
