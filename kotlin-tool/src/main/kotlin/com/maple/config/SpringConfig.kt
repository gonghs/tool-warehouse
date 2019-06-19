package com.maple.config;

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * spring配置
 *
 * @author maple
 * @version 1.0
 * @since 2019-06-20 01:44
 */
@Configuration
@ComponentScan(basePackages = ["com.maple"])
@EnableJpaRepositories(basePackages = ["com.maple.repository"])
open class SpringConfig {

}
