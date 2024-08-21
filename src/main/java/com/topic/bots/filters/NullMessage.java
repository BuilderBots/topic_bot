package com.topic.bots.filters;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
public class NullMessage extends BotApiMethod<Serializable> {

    @Override
    public Serializable deserializeResponse(String answer) {
        return null;
    }

    @Override
    public String getMethod() {
        return "";
    }
}
