package com.topic.bots.config;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public interface MultiThreadUpdateConsumer extends LongPollingUpdateConsumer {

    ThreadPoolExecutor EX = ExecutorBuilder.create()
            .setCorePoolSize(2)
            .setMaxPoolSize(100)
            .setThreadFactory(ThreadFactoryBuilder.create()
                    .setNamePrefix("long_poll_")
                    .build())
            .build();

    default void consume(List<Update> updates) {
        updates.forEach((update) -> EX.execute(() -> this.consume(update)));
    }


    void consume(Update updates);
}
