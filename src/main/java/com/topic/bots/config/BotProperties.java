package com.topic.bots.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bots.config")
public class BotProperties {

    private boolean logs;

    /** 代理 **/
    private boolean enableProxy;
    private String proxyType = "";
    private Integer proxyPort = 0;
    private String proxyHostName = "";
    /** 代理 **/

    private String greetings;
    private Long backgroundId;
    /** 客服群ID **/
    private Long forwardId;
    /** 转发消息 topic 名字 **/
    private String topicName;

    private Map<String, String> tokens;


    public String getToken () {
        return this.tokens.get(Constants.TOKEN_KEY);
    }

    public boolean background (Long chatId) {
        return Objects.equals(chatId, this.backgroundId);
    }
}
