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
 *     关键词
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Setter
@Getter
@NoArgsConstructor
@TableName("t_keywords")
@Accessors(chain = true)
public class Keywords {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String keyword;

    private String content;

}
