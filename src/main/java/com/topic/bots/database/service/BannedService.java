package com.topic.bots.database.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.topic.bots.database.entity.BannedUser;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public interface BannedService extends IService<BannedUser> {

    void insertBannedUser(BannedUser b);

    BannedUser getBannedUser(Long userId);
}
