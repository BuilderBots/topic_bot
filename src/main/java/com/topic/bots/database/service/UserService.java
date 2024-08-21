package com.topic.bots.database.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.topic.bots.database.entity.User;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public interface UserService extends IService<User> {
    User selectUserId(Long chatId);

    void updateAny(User user);
}
