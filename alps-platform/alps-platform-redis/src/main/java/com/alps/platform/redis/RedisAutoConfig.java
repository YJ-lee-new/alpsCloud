package com.alps.platform.redis;

//import java.time.Duration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.cache.CacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import com.alps.platform.redis.serializer.FastJsonRedisSerializer;
//import com.alps.platform.redis.util.RedisUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
///**
// * @author:Yujie.lee
// * Date:2019年11月21日
// * Todo redis自动装配
// */
//@Configuration
////@AutoConfigureBefore(RedisTemplate.class)
//public class RedisAutoConfig {
//
//	
//	@Autowired(required=false)  
//	private LettuceConnectionFactory lettuceConnectionFactory;
//
//	/**
//	 * 适配redis cluster单节点
//	 * @return
//	 */
////	@Primary
////	@Bean("redisTemplate")
////	// 没有此属性就不会装配bean 如果是单个redis 将此注解注释掉
////	@ConditionalOnProperty(name = "spring.redis.cluster.nodes", matchIfMissing = false)
////	public RedisTemplate<String, Object> getRedisTemplate() {
////		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
////		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
////
////		RedisSerializer<?> stringSerializer = new StringRedisSerializer();
////		// RedisSerializer redisObjectSerializer = new RedisObjectSerializer();
////		RedisSerializer<?> redisObjectSerializer = new RedisObjectSerializer();
////		redisTemplate.setKeySerializer(stringSerializer); // key的序列化类型
////		redisTemplate.setHashKeySerializer(stringSerializer);
////		redisTemplate.setValueSerializer(redisObjectSerializer); // value的序列化类型
////		redisTemplate.setHashValueSerializer(redisObjectSerializer); // value的序列化类型
////		redisTemplate.afterPropertiesSet();
////
////		redisTemplate.opsForValue().set("hello", "wolrd");
////		return redisTemplate;
////	}
////	
////	/**
////	 * 适配redis单节点
////	 * @return
////	 */
////	@Primary
////	@Bean("redisTemplate")
////	@ConditionalOnProperty(name = "spring.redis.host", matchIfMissing = true)
////	public RedisTemplate<String, Object> getSingleRedisTemplate( ) {
////		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
////        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
////        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
////        FastJsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJsonRedisSerializer<Object> (Object.class);
////        ObjectMapper objectMapper = new ObjectMapper();
////        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
////        fastJson2JsonRedisSerializer.setObjectMapper(objectMapper);
//// 
////        // key采用String的序列化方式
////        redisTemplate.setKeySerializer(stringRedisSerializer);
////        // string的value采用fastJson序列化方式
////        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
////        // hash的key也采用String的序列化方式
////        redisTemplate.setHashKeySerializer(stringRedisSerializer);
////        // hash的value采用fastJson序列化方式
////        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
////        redisTemplate.afterPropertiesSet();
////        return redisTemplate;
////
////	}
////	
//	
//   @Bean
//   public CacheManager cacheManager() {
//	   RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//	   Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//	
//	   //解决查询缓存转换异常的问题
//	   ObjectMapper om = new ObjectMapper();
//	   om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//	   om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//	   jackson2JsonRedisSerializer.setObjectMapper(om);
//	   Jackson2JsonRedisSerializer<Object> fastJsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//	   
//	   // 配置序列化（解决乱码的问题）
//	   RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//           .entryTtl(Duration.ofHours(1))
//           .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
//           .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer))
//           .disableCachingNullValues();
//       
//       RedisCacheManager cacheManager = RedisCacheManager.builder(lettuceConnectionFactory)
//               .cacheDefaults(config)
//               .build();
//       return cacheManager;
//   }
//	
//	
//    @Bean
//    public HashOperations<String, String, String> hashOperations(StringRedisTemplate stringRedisTemplate) {
//        return stringRedisTemplate.opsForHash();
//    }
//    
//    /**
//     * 对redis字符串类型数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForValue();
//    }
//
//    /**
//     * 对链表类型的数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForList();
//    }
//
//    /**
//     * 对无序集合类型的数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForSet();
//    }
//
//    /**
//     * 对有序集合类型的数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForZSet();
//    }
//    
//	/**
//	 * redis工具类
//	 */
//	@Bean("redisUtil")
//	public RedisUtil redisUtil( RedisTemplate<String, Object>  redisTemplate ,StringRedisTemplate stringRedisTemplate,HashOperations<String, String, String> hashOperations) {
//		RedisUtil redisUtil = new RedisUtil(redisTemplate ,stringRedisTemplate , hashOperations); 
//		return redisUtil;
//	}
//	
//}
