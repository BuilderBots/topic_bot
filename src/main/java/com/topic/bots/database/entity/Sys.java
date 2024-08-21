package com.topic.bots.database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@TableName("t_system")
@Accessors(chain = true)
public class Sys {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 欢迎语 **/
    private String greetings;

    /** 底部键盘 **/
    private String replyKeyboard;

    /** 后台群ID **/
    private Long backgroundId;

    /** 转发群ID **/
    private Long forwardId;

    /** 转发消息 topic 名字 **/
    private String topicName;

    /** 结束语 **/
    private String finishText;

    /** 消息头部 **/
    private String messageTitle;

    /** 消息尾巴 **/
    private String messageTail;

    /** 被封号用户看到的信息 **/
    private String bannedText;

    /** 话题复用 **/
    private Boolean topicReuse;

    public Sys (Long backgroundId) {
        this.backgroundId = backgroundId;
    }

}
