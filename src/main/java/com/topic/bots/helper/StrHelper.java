package com.topic.bots.helper;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.topic.bots.common.Commands;
import com.topic.bots.common.CommonCache;

/**
 * <p>
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public class StrHelper {

    public static String nickname (String first, String last) {
        return first + (StrUtil.isBlank(last) ? "" : " " + last);
    }


    // 通过值替换占位符
    // 客户 | %nickname% | %userId% | 新对话
    public static String replace (String text, String username, String nickname, Long userId) {
        if (StrUtil.contains(text, Commands.USERNAME)) {
            text = StrUtil.replace(text, Commands.USERNAME, username);
        }
        if (StrUtil.contains(text, Commands.NICKNAME)) {
            text = StrUtil.replace(text, Commands.NICKNAME, nickname);
        }
        if (StrUtil.contains(text, Commands.USER_ID)) {
            text = StrUtil.replace(text, Commands.USER_ID, String.valueOf(userId));
        }
        return text;
    }


    public static String comboMessage (String body){
        StrBuilder result = new StrBuilder();
        if (StrUtil.isNotBlank(body)) {
            String messageTitle = CommonCache.getSystem().getMessageTitle();
            if (StrUtil.isNotBlank(messageTitle)) {
                result.append(messageTitle).append("\n\n").append(body).append("\n\n");
            }
        }
        String messageTail = CommonCache.getSystem().getMessageTail();
        if (StrUtil.isNotBlank(messageTail)) {
            result.append(messageTail);
        }
        return result.toString();
    }
}
