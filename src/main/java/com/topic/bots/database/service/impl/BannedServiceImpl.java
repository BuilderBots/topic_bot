package com.topic.bots.database.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.topic.bots.database.entity.BannedUser;
import com.topic.bots.database.mapper.BannedMapper;
import com.topic.bots.database.service.BannedService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Service
public class BannedServiceImpl extends ServiceImpl<BannedMapper, BannedUser> implements BannedService {

    @Override
    public void insertBannedUser(BannedUser b) {
        this.baseMapper.insert(b);
    }

    @Override
    public BannedUser getBannedUser(Long userId) {
        return this.baseMapper.selectOne(
                Wrappers.<BannedUser>lambdaQuery()
                        .eq(BannedUser::getUserId, userId)
                        .orderByDesc(BannedUser::getUserId)
                        .last(" limit 1")
        );
    }
}
