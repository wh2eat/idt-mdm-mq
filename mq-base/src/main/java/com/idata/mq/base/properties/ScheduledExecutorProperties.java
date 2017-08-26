package com.idata.mq.base.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScheduledExecutorProperties {

    @Value("${mq.scheduledThreadPoolExecutor.corePoolSize}")
    private int corePoolSize;

    @Value("${mq.scheduledThreadPoolExecutor.keepAliveSeconds}")
    private int keepAliveSeconds;

    @Value("${mq.scheduledThreadPoolExecutor.maximumPoolSize}")
    private int maximumPoolSize;

    public ScheduledExecutorProperties() {
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

}
