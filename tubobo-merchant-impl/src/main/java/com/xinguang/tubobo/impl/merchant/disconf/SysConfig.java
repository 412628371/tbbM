package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by yanxu on 2017/9/28.
 *  系统参数配置类，包括数据库、mq、redis等参数.
 */


@Scope("singleton")
@DisconfFile(filename = "common.properties")
public class SysConfig {

    private String jdbcSourceUrl;
    private String jdbcSourceUserName;
    private String jdbcSourcePassword;

    private String redisHost;
    private int redisPort;
    private String redisPassword;
    private int redisDbIndex;

    private String rabbitmqAddress;
    private String rabbitmqUsername;
    private String rabbitmqPassword;
    private String rabbitmqVirtualHost;


    public String getJdbcSourceUrl() {
        return jdbcSourceUrl;
    }

    public void setJdbcSourceUrl(String jdbcSourceUrl) {
        this.jdbcSourceUrl = jdbcSourceUrl;
    }

    public String getJdbcSourceUserName() {
        return jdbcSourceUserName;
    }

    public void setJdbcSourceUserName(String jdbcSourceUserName) {
        this.jdbcSourceUserName = jdbcSourceUserName;
    }

    public String getJdbcSourcePassword() {
        return jdbcSourcePassword;
    }

    public void setJdbcSourcePassword(String jdbcSourcePassword) {
        this.jdbcSourcePassword = jdbcSourcePassword;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public int getRedisDbIndex() {
        return redisDbIndex;
    }

    public void setRedisDbIndex(int redisDbIndex) {
        this.redisDbIndex = redisDbIndex;
    }

    public String getRabbitmqAddress() {
        return rabbitmqAddress;
    }

    public void setRabbitmqAddress(String rabbitmqAddress) {
        this.rabbitmqAddress = rabbitmqAddress;
    }

    public String getRabbitmqUsername() {
        return rabbitmqUsername;
    }

    public void setRabbitmqUsername(String rabbitmqUsername) {
        this.rabbitmqUsername = rabbitmqUsername;
    }

    public String getRabbitmqPassword() {
        return rabbitmqPassword;
    }

    public void setRabbitmqPassword(String rabbitmqPassword) {
        this.rabbitmqPassword = rabbitmqPassword;
    }

    public String getRabbitmqVirtualHost() {
        return rabbitmqVirtualHost;
    }

    public void setRabbitmqVirtualHost(String rabbitmqVirtualHost) {
        this.rabbitmqVirtualHost = rabbitmqVirtualHost;
    }
}
