package com.xinguang.tubobo;

import com.xinguang.tubobo.checking.TbbCheckingConstants;
import com.xinguang.tubobo.impl.merchant.disconf.SysConfig;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.stereotype.Service;



/**
 * @author xqh
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource("classpath:spring-context.xml")
@EnableRabbit
@Service
@EnableJpaAuditing
public class Application {

    public final static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired
    private SysConfig config;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Primary
   // @ConfigurationProperties(prefix="datasource.primary")
    public DataSource dataSource() {
        DataSource ds = new DataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(config.getJdbcSourceUrl());
        ds.setUsername(config.getJdbcSourceUserName());
        ds.setPassword(config.getJdbcSourcePassword());

        ds.setTestWhileIdle(true);
        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(true);
        ds.setValidationQuery("SELECT 1");

        return ds;
    }
    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {

        return new HibernateJpaSessionFactoryBean();
    }
    @Bean
    public Queue dataCheckingQueue() {
        return QueueBuilder.durable(TbbCheckingConstants.DATA_CHECKING_QUEUE).build();
    }

 /*   @Bean
    public RedisUtils getRedisUtils(){
        RedisUtils redisUtils = new RedisUtils("tbb_consignor", "consignor", getRedisTemplate(), 86400);

        return redisUtils;
    }*/

   /* @Bean
    @SuppressWarnings("rawtypes")
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }*/

   /* @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }*/

   /* @Bean
    public RedisTemplate<?, ?> getRedisTemplate(){
        RedisTemplate<?,?> template = new StringRedisTemplate(redisConnectionFactory());
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();

        return template;
    }*/

   /* @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();

        factory.setHostName(config.getRedisHost());
        factory.setPort(config.getRedisPort());
        factory.setPassword(config.getRedisPassword());
        factory.setDatabase(config.getRedisDbIndex());
        factory.setUsePool(true);

        factory.afterPropertiesSet();

        return factory;
    }*/

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setAddresses(config.getRabbitmqAddress());
//        connectionFactory.setUsername(config.getRabbitmqUsername());
//        connectionFactory.setPassword(config.getRabbitmqPassword());
//        connectionFactory.setVirtualHost(config.getRabbitmqVirtualHost());
//
//        return connectionFactory;
//    }

}
