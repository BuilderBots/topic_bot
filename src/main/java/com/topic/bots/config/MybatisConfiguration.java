package com.topic.bots.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since v 0.0.1
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.topic.bots.database.mapper")
public class MybatisConfiguration {


}
