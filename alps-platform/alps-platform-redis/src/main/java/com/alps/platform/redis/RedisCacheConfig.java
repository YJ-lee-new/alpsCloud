/**
 *@date:2020年1月5日
 *@author:Yujie.Lee
 *TODO
 */
package com.alps.platform.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alps.platform.redis.serializer.FastJsonRedisSerializer;
import com.alps.platform.redis.serializer.RedisObjectSerializer;
import com.alps.platform.redis.util.RedisUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author:Yujie.lee
 * Date:2020年1月5日
 * TodoTODO
 */
@Configuration
@ConfigurationProperties(prefix = "spring.cache.redis")
@AutoConfigureBefore(RedisTemplate.class)
public class RedisCacheConfig {
	
	@Autowired(required=false)  
	private LettuceConnectionFactory lettuceConnectionFactory;
	
    private Duration timeToLive = Duration.ofHours(1);
    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }
    
	  @Bean("redisTemplate")
	  public RedisTemplate <String, Object> redisTemplate() {
	      RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
	      redisTemplate.setConnectionFactory(lettuceConnectionFactory);
	      /*
	       * 使用aili的jackson序例时,得启服务后第一次请求接口会报"default constructor not found",网上说是缺少默认空的构造函数,但试过
	       * 不成立,原因待查.
	       */
	      RedisSerializer<String> redisSerializer = new StringRedisSerializer();

	      FastJsonRedisSerializer<Object> jackson2JsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
//	      RedisObjectSerializer jackson2JsonRedisSerializer = new RedisObjectSerializer();
	      redisTemplate.setKeySerializer(redisSerializer);
	      redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
	      redisTemplate.setHashKeySerializer(redisSerializer);
	      redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
	      redisTemplate.afterPropertiesSet();
	      return redisTemplate;
	  }
    
    @Bean
    public CacheManager cacheManager() {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//      RedisObjectSerializer jackson2JsonRedisSerializer = new RedisObjectSerializer();
        FastJsonRedisSerializer<Object> jackson2JsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
      //解决查询缓存转换异常的问题
 	   ObjectMapper om = new ObjectMapper();
 	   om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
 	   om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
 	   jackson2JsonRedisSerializer.setObjectMapper(om);
 
        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
 
        RedisCacheManager cacheManager = RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }
    
  @Bean
  @ConditionalOnMissingBean(RedisUtil.class)
  @ConditionalOnBean(StringRedisTemplate.class)
  public RedisUtil<?>  redisUtil(StringRedisTemplate stringRedisTemplate) {
      RedisUtil<?> redisUtils =  new RedisUtil(stringRedisTemplate);
      return redisUtils;
  }
 
}
