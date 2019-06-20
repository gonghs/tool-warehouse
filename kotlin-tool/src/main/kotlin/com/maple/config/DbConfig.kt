package com.maple.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


/**
 * 数据库配置
 *
 * @author maple
 * @version 1.0
 * @since 2019-06-20 01:41
 */
@Configuration
@EnableTransactionManagement
open class DbConfig {

    @Bean
    open fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource()
        // 把 package names 修改为你的 User 类所在的包名就可以了
        em.setPackagesToScan("com.maple.jpa.entity")
        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        em.setJpaProperties(additionalProperties())
        return em
    }

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("com.mysql.jdbc.Driver")
        dataSource.url = "jdbc:mysql://localhost:3306/maple"
        dataSource.username = "maple"
        dataSource.password = "maple"
        return dataSource
    }

    @Bean
    open fun transactionManager(
        emf: EntityManagerFactory
    ): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = emf
        return transactionManager
    }

    @Bean
    open fun exceptionTranslation(): PersistenceExceptionTranslationPostProcessor {
        return PersistenceExceptionTranslationPostProcessor()
    }

    private fun additionalProperties(): Properties {
        val properties = Properties()
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop")
        properties.setProperty(
            "hibernate.dialect",
            "org.hibernate.dialect.MySQL5Dialect"
        )
        return properties
    }
}