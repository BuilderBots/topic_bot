package com.topic.bots.handlers;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.topic.bots.common.CommonCache;
import com.topic.bots.config.BotProperties;
import com.topic.bots.database.entity.BannedUser;
import com.topic.bots.database.entity.Keywords;
import com.topic.bots.database.entity.Sys;
import com.topic.bots.database.entity.User;
import com.topic.bots.database.enums.BannedStatus;
import com.topic.bots.database.service.BannedService;
import com.topic.bots.database.service.KeywordsService;
import com.topic.bots.database.service.SystemService;
import com.topic.bots.database.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Component
public class InitHandler {

    @Resource private UserService userService;
    @Resource private BotProperties properties;
    @Resource private SystemService systemService;
    @Resource private BannedService bannedService;
    @Resource private KeywordsService keywordsService;

    @PostConstruct
    public void doInit () {
        this.loadSystem();
        this.loadUserTopic();
        this.loadBannedUser();
        this.loadKeywords();
    }

    private void loadKeywords() {
        List<Keywords> list = this.keywordsService.list();
        for (Keywords keywords : list) {
            CommonCache.setKeywords(keywords.getKeyword(), keywords.getContent());
        }
    }

    private void loadBannedUser() {
        List<BannedUser> users = this.bannedService.list(
                Wrappers.<BannedUser>lambdaQuery()
                        .eq(BannedUser::getBannedStatus, BannedStatus.BANNED)
        );

        for (BannedUser user : users) {
            CommonCache.banned(user.getUserId());
        }
    }

    private void loadUserTopic() {
        List<User> list = this.userService.list(
                Wrappers.<User>lambdaQuery()
                        .isNotNull(User::getTopicId)
        );
        for (User user : list) {
            CommonCache.setUserTopicId(user.getUserId(), user.getTopicId());
        }
    }

    private void loadSystem() {
        // 永远加载最新的一个
        Sys sys = systemService.selectSystem();

        if (Objects.isNull(sys)) {
            sys = new Sys()
                    .setGreetings(properties.getGreetings())
                    .setBackgroundId(properties.getBackgroundId())
                    .setForwardId(properties.getForwardId())
                    .setTopicName(properties.getTopicName())
                    .setReplyKeyboard("");
            systemService.save(sys);
        }
        CommonCache.setSystem(sys);
    }

    public void flush() {
        CommonCache.flush();
        this.doInit();
    }
}
