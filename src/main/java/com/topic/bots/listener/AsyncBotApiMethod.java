package com.topic.bots.listener;

import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.json.JSONUtil;
import com.topic.bots.config.BotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;


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
public class AsyncBotApiMethod {

    @Resource private BotProperties properties;
    @Resource private TelegramClient telegramClient;

    private static final LinkedBlockingQueue<PartialBotApiMethod<?>> QUEUE = new LinkedBlockingQueue<>();

    public static void addMessage (PartialBotApiMethod<?> message) {
        if (Objects.isNull(message)) {
            return;
        }
        QUEUE.add(message);
    }

    public AsyncBotApiMethod() {
        this.run();
    }


    private void run() {
        GlobalThreadPool.execute(() -> {
            while (!Thread.interrupted()) {
                try {
                    PartialBotApiMethod<?> take = QUEUE.take();
                    if (this.properties.isLogs()) {
                        log.info("【异步】发送：{}", JSONUtil.toJsonStr(take));
                    }
                    if (take instanceof SendMessage) {
                        telegramClient.execute((SendMessage) take);
                        continue;
                    }
                    if (take instanceof SendPhoto ) {
                        telegramClient.execute((SendPhoto) take);
                        continue;
                    }
                    if (take instanceof SendDocument) {
                        telegramClient.execute((SendDocument) take);
                        continue;
                    }
                    if (take instanceof EditMessageText) {
                        telegramClient.execute((EditMessageText) take);
                        continue;
                    }
                    if (take instanceof SendDice) {
                        telegramClient.execute((SendDice) take);
                        continue;
                    }
                    if (take instanceof SendVideo) {
                        telegramClient.execute((SendVideo) take);
                        continue;
                    }
                    if (take instanceof SendAudio) {
                        telegramClient.execute((SendAudio) take);
                        continue;
                    }
                    if (take instanceof SendAnimation) {
                        telegramClient.execute((SendAnimation) take);
                    }
                } catch (InterruptedException | TelegramApiException e) {
                    log.error("【异步】异常：{}", e.getMessage(), e);
                }
            }
        });
    }
}
