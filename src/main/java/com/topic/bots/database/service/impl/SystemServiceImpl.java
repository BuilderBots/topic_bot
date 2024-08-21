package com.topic.bots.database.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.topic.bots.common.CommonCache;
import com.topic.bots.database.entity.Sys;
import com.topic.bots.database.mapper.SystemMapper;
import com.topic.bots.database.service.SystemService;
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
public class SystemServiceImpl extends ServiceImpl<SystemMapper, Sys> implements SystemService {
    @Override
    public Sys selectSystem() {
        return this.getOne(
                Wrappers.<Sys>lambdaQuery()
                        .orderByDesc(Sys::getId)
                        .last(" limit 1")
        );
    }

    @Override
    public void updateAny(Sys sys) {
        if (Objects.isNull(sys.getBackgroundId())) {
            throw new IllegalArgumentException("后群ID不能为空");
        }

        this.baseMapper.update(
                Wrappers.<Sys>lambdaUpdate()
                        .set(StrUtil.isNotBlank(sys.getGreetings()), Sys::getGreetings, sys.getGreetings())
                        .set(StrUtil.isNotBlank(sys.getReplyKeyboard()), Sys::getReplyKeyboard, sys.getReplyKeyboard())
                        .set(Objects.nonNull(sys.getForwardId()), Sys::getForwardId, sys.getForwardId())
                        .set(StrUtil.isNotBlank(sys.getTopicName()), Sys::getTopicName, sys.getTopicName())
                        .set(StrUtil.isNotBlank(sys.getMessageTitle()), Sys::getMessageTitle, sys.getMessageTitle())
                        .set(StrUtil.isNotBlank(sys.getMessageTail()), Sys::getMessageTail, sys.getMessageTail())
                        .set(StrUtil.isNotBlank(sys.getFinishText()), Sys::getFinishText, sys.getFinishText())
                        .set(StrUtil.isNotBlank(sys.getBannedText()), Sys::getBannedText, sys.getBannedText())
                        .set(Objects.nonNull(sys.getTopicReuse()), Sys::getTopicReuse, sys.getTopicReuse())
                        .eq(Sys::getBackgroundId, sys.getBackgroundId())
        );

        // 更新缓存
        if (StrUtil.isNotBlank(sys.getGreetings())) {
            CommonCache.getSystem().setGreetings(sys.getGreetings());
        }
        if (StrUtil.isNotBlank(sys.getReplyKeyboard())) {
            CommonCache.getSystem().setReplyKeyboard(sys.getReplyKeyboard());
        }
        if (Objects.nonNull(sys.getForwardId())) {
            CommonCache.getSystem().setForwardId(sys.getForwardId());
        }
        if (StrUtil.isNotBlank(sys.getTopicName())) {
            CommonCache.getSystem().setTopicName(sys.getTopicName());
        }
        if (StrUtil.isNotBlank(sys.getMessageTitle())) {
            CommonCache.getSystem().setMessageTitle(sys.getMessageTitle());
        }
        if (StrUtil.isNotBlank(sys.getMessageTail())) {
            CommonCache.getSystem().setMessageTail(sys.getMessageTail());
        }
        if (StrUtil.isNotBlank(sys.getFinishText())) {
            CommonCache.getSystem().setFinishText(sys.getFinishText());
        }
        if (StrUtil.isNotBlank(sys.getBannedText())) {
            CommonCache.getSystem().setBannedText(sys.getBannedText());
        }
        if (Objects.nonNull(sys.getTopicReuse())) {
            CommonCache.getSystem().setTopicReuse(sys.getTopicReuse());
        }
    }
}
