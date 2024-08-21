package com.topic.bots.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.topic.bots.database.entity.Keywords;
import com.topic.bots.database.mapper.KeywordsMapper;
import com.topic.bots.database.service.KeywordsService;
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
public class KeywordsServiceImpl extends ServiceImpl<KeywordsMapper, Keywords> implements KeywordsService {
}
