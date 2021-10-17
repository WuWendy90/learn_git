package redis;

import java.util.Collections;

public class RedisTool {
	private static final Long RELEASE_SUCCESS = 1L;
    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    
    private long limitFlow(String key) {
        //Setnx（SET if Not eXists） 命令在指定的 key 不存在时，为 key 设置指定的值。设置成功返回1，设置失败返回0
        Long lng = redisCacheClient.setnx(jedisGroup, key, "1");
        if (lng == 1) {
            //设置时间窗口，redis-key时效为10秒
            redisCacheClient.expire(jedisGroup, key, 10);
            return 1L;
        } else {
            //Redis Incrby 命令将 key 中储存的数字加上指定的增量值。相当于放在redis中的计数器，每次请求到来计数器自增1
            long val = redisCacheClient.incrBy(jedisGroup, key, 1);
  
            return val;
        }
    }

    
    
}
