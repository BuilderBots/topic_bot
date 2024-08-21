package com.topic.bots;

import cn.hutool.json.JSONUtil;
import com.topic.bots.config.BotProperties;
import com.topic.bots.config.MultiThreadUpdateConsumer;
import com.topic.bots.handlers.AbstractHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Slf4j
@Component
public class TopicForwardBot implements SpringLongPollingBot, MultiThreadUpdateConsumer {

    @Resource private BotProperties properties;
    @Resource private TelegramClient telegramClient;

    @Override
    public void consume(Update update) {
        try {
            if (this.properties.isLogs()) {
                log.info("消息：{}", JSONUtil.toJsonStr(update));
            }
            BotApiMethod<?> message = AbstractHandler.doExecute(update);
            if (Objects.nonNull(message)) {
                if (this.properties.isLogs()) {
                    log.info("回复：{}", JSONUtil.toJsonStr(message));
                }
                Serializable result = this.telegramClient.execute(message);
                if (this.properties.isLogs()) {
                    log.info("响应：{}", JSONUtil.toJsonStr(result));
                }
            }
        } catch (TelegramApiException e) {
            log.error("发送消息失败：{}", e.getMessage(), e);
        }
    }


    @Override
    public String getBotToken() {
        return this.properties.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("[机器人状态] {}", botSession.isRunning());
    }

}
