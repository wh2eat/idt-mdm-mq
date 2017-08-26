package com.idata.mq.base;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.CacheMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.idata.mq.base.properties.AmqProperties;
import com.idata.mq.base.properties.ScheduledExecutorProperties;

@Configuration
public class BaseConfigurator {

    private final static Logger logger = LogManager.getLogger(BaseConfigurator.class);

    @Autowired
    private AmqProperties amqProperties;

    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {

        if (logger.isDebugEnabled()) {
            logger.debug("[][][" + amqProperties + "]");
        }

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(amqProperties.getHost());
        connectionFactory.setPort(amqProperties.getPort());
        connectionFactory.setUsername(amqProperties.getUsername());
        connectionFactory.setPassword(amqProperties.getPassword());
        connectionFactory.setVirtualHost(amqProperties.getVirtualHost());

        connectionFactory.setPublisherConfirms(true); // 必须要设置
        connectionFactory.setPublisherReturns(true);

        connectionFactory.setCacheMode(CacheMode.CONNECTION);
        connectionFactory.setConnectionCacheSize(30);
        connectionFactory.setConnectionTimeout(60 * 1000);

        connectionFactory.setExecutor(createScheduledThreadPoolExecutor());

        if (logger.isDebugEnabled()) {
            logger.debug("[][connectionFactory][" + connectionFactory + "][" + amqProperties.getHost() + ","
                    + amqProperties.getPort() + "," + amqProperties.getUsername() + "," + amqProperties.getPassword()
                    + "," + amqProperties.getVirtualHost() + "]");
        }

        return connectionFactory;
    }

    @Autowired
    private ScheduledExecutorProperties scheduledExecutorProperties;

    @Bean(name = "scheduledExecutor")
    public ScheduledThreadPoolExecutor createScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                scheduledExecutorProperties.getCorePoolSize(), new ThreadPoolExecutor.CallerRunsPolicy());

        executor.setKeepAliveTime(scheduledExecutorProperties.getKeepAliveSeconds(), TimeUnit.SECONDS);
        executor.setMaximumPoolSize(scheduledExecutorProperties.getMaximumPoolSize());
        return executor;
    }

    @Bean(name = "jsonMessageConverter")
    public MessageConverter createMessageConverter() {
        return new FastJsonMessageConverter();
    }

    // 增加失败重试机制
    // 发送失败之后，会尝试重发三次，重发间隔(ms)
    // 第一次 initialInterval 此后：initialInterval*multiplier > maxInterval ? maxInterval :
    // initialInterval*multiplier。
    // 配合集群使用的时候，当mq集群中一个down掉之后，重试机制尝试其他可用的mq。
    @Bean(name = "retryConnTemplate")
    public RetryTemplate createRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();

        policy.setInitialInterval(500);
        policy.setMultiplier(10.0);
        policy.setMaxInterval(5000);

        retryTemplate.setBackOffPolicy(policy);

        return retryTemplate;
    }
}
