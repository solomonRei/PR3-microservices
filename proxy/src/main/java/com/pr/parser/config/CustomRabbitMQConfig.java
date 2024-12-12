package com.pr.parser.config;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CustomRabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory(Environment env) {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(env.getProperty("spring.rabbitmq.host", "rabbitmq1"));
        factory.setPort(env.getProperty("spring.rabbitmq.port", Integer.class, 5672));
        factory.setUsername(env.getProperty("spring.rabbitmq.username", "test"));
        factory.setPassword(env.getProperty("spring.rabbitmq.password", "test"));
        return factory;
    }
}

