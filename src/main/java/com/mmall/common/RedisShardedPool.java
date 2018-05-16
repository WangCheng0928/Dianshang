package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangcheng
 * @Date: Created in 20:47 2018/5/15
 */
@Slf4j
public class RedisShardedPool {

    private static ShardedJedisPool pool;   //sharded jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));  //最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10")); //在jedispool中最大的idle状态（空闲的）的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2")); // 在jedispool中最小的idle状态（空闲的）的jedis实例的个数
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true")); // 在borrow一个jedis实例的时候，是否进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true")); // 在return一个jedis实例的时候，是否进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true); //连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时，默认为true

        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redis1Ip, redis1Port, 1000*2);
//        jedisShardInfo1.setPassword(); //如果有密码，则可以设置密码
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(redis2Ip, redis2Port, 1000*2);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(jedisShardInfo1);
        jedisShardInfoList.add(jedisShardInfo2);
        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN); //分片策略为 一致性hash ，还有一种是MD5
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokensource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }

//    public static void main(String[] args) {
//        ShardedJedis jedis = pool.getResource();
//        for (int i=0; i < 10 ; i++){
//            jedis.set("key"+i, "value"+i);
//        }
//        returnResource(jedis);
////        pool.destroy(); //临时调用，销毁连接池中的所有连接
//        System.out.println("program is end");
//    }
}