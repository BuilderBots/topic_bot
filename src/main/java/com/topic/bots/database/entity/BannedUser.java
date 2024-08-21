package com.topic.bots.database.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.topic.bots.database.enums.BannedStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Setter
@Getter
@NoArgsConstructor
@TableName("t_banned")
@Accessors(chain = true)
public class BannedUser {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long userId;

    // 发言
    private String speech;

    // 触发的首个关键词
    private String keywords;

    private LocalDateTime bannedTime;

    private BannedStatus bannedStatus;

    public BannedUser(Long userId, String reason) {
        this.userId = userId;
        this.bannedTime = LocalDateTime.now();
        this.bannedStatus = BannedStatus.BANNED;
        this.speech = "客服封禁";
        this.keywords = StrUtil.isBlank(reason) ? this.speech : reason;
    }
}
