package com.topic.bots.database.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Getter
@AllArgsConstructor
public enum BannedStatus {

    UNBANNED(0),
    BANNED(1);

    @EnumValue
    private final Integer code;

    public static boolean isBanned(BannedStatus status) {
        return BANNED.equals(status);
    }
}
