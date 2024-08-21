package com.topic.bots.database.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.topic.bots.database.entity.Sys;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public interface SystemService extends IService<Sys> {

    Sys selectSystem();

    void updateAny(Sys sys);
}
