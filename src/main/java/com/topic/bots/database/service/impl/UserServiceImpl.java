package com.topic.bots.database.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.topic.bots.database.entity.Sys;
import com.topic.bots.database.entity.User;
import com.topic.bots.database.mapper.UserMapper;
import com.topic.bots.database.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User selectUserId(Long chatId) {
        return this.baseMapper.selectOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getUserId, chatId)
                        .last(" limit 1")
        );
    }

    @Override
    public void updateAny(User user) {
        if (Objects.isNull(user.getUserId())) {
            throw new IllegalArgumentException("UID不能为空");
        }

        this.baseMapper.update(
                Wrappers.<User>lambdaUpdate()
                        .set(Objects.nonNull(user.getTopicId()), User::getTopicId, user.getTopicId())
                        .eq(User::getUserId, user.getUserId())
        );
    }
}
