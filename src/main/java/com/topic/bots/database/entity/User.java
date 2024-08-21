package com.topic.bots.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
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
@TableName("t_user")
@Accessors(chain = true)
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private String username;

    private String nickname;

    private Integer topicId;
}
