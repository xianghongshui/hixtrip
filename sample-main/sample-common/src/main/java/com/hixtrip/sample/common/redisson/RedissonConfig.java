package com.hixtrip.sample.common.redisson;



import com.hixtrip.sample.common.redisson.aspect.LockAspect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
/**
 * @author xianghs
 */
@Slf4j
@ConditionalOnClass({RedissonClient.class, Redisson.class})
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {
    private static final String REDIS_PROTOCOL_PREFIX = "redis://";
    private static final String REDISS_PROTOCOL_PREFIX = "rediss://";
    private RedisProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public LockAspect lockAspect(RedissonClient redissonClient){
        return new LockAspect(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(@Qualifier("spring.data.redis-org.springframework.boot.autoconfigure.data.redis.RedisProperties") RedisProperties properties){
        this.properties = properties;
        log.debug("尝试初始化RedissonClient");
        // 1.读取Redis配置
        RedisProperties.Cluster cluster = properties.getCluster();
        RedisProperties.Sentinel sentinel = properties.getSentinel();
        String password = properties.getPassword();
        int timeout = 3000;
        Duration d = properties.getTimeout();
        if(d != null){
            timeout = Long.valueOf(d.toMillis()).intValue();
        }
        // 2.设置Redisson配置
        Config config = new Config();
        if(cluster != null && !CollectionUtils.isEmpty(cluster.getNodes())){
            // 集群模式
            config.useClusterServers()
                    .addNodeAddress(convert(cluster.getNodes()))
                    .setConnectTimeout(timeout)
                    .setPassword(password);
        }else if(sentinel != null && !StringUtils.isEmpty(sentinel.getMaster())){
            // 哨兵模式
            config.useSentinelServers()
                    .setMasterName(sentinel.getMaster())
                    .addSentinelAddress(convert(sentinel.getNodes()))
                    .setConnectTimeout(timeout)
                    .setDatabase(0)
                    .setPassword(password);
        }else{
            // 单机模式
            config.useSingleServer()
                    .setAddress(String.format("redis://%s:%d", properties.getHost(), properties.getPort()))
                    .setConnectTimeout(timeout)
                    .setDatabase(0)
                    .setPassword(password);
        }
        // 3.创建Redisson客户端
        return Redisson.create(config);
    }

    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
                nodes.add(REDIS_PROTOCOL_PREFIX + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[0]);
    }
}
