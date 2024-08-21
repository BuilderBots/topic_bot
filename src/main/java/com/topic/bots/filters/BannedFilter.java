package com.topic.bots.filters;

import cn.hutool.core.util.StrUtil;
import com.topic.bots.common.CommonCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 *      用户封禁
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Component
public class BannedFilter extends AbstractFilter {

    @Override
    public BotApiMethod<?> filter(Update update) {
        Long userId = null;
        if (update.hasMessage()) {
            userId = update.getMessage().getFrom().getId();
        }
        // 点击按钮
        if (update.hasCallbackQuery()) {
            userId = update.getCallbackQuery().getFrom().getId();
        }

        if (CommonCache.hasBanned(userId)) {
            String bannedText = CommonCache.getSystem().getBannedText();
            if (StrUtil.isNotBlank(bannedText)) {
                return fail(update.getMessage(), bannedText);
            }
            return CommonCache.NULL_MESSAGE;
        }
        return null;
    }
}
