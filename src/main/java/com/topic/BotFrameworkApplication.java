package com.topic;

import com.topic.bots.config.EnableBot;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EnableBot
@SpringBootApplication
public class BotFrameworkApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BotFrameworkApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}
}
