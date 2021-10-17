package redis;

import java.util.Collections;

public class RedisTool {
	private static final Long RELEASE_SUCCESS = 1L;
    /**
     * �ͷŷֲ�ʽ��
     * @param jedis Redis�ͻ���
     * @param lockKey ��
     * @param requestId �����ʶ
     * @return �Ƿ��ͷųɹ�
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
        //Setnx��SET if Not eXists�� ������ָ���� key ������ʱ��Ϊ key ����ָ����ֵ�����óɹ�����1������ʧ�ܷ���0
        Long lng = redisCacheClient.setnx(jedisGroup, key, "1");
        if (lng == 1) {
            //����ʱ�䴰�ڣ�redis-keyʱЧΪ10��
            redisCacheClient.expire(jedisGroup, key, 10);
            return 1L;
        } else {
            //Redis Incrby ��� key �д�������ּ���ָ��������ֵ���൱�ڷ���redis�еļ�������ÿ������������������1
            long val = redisCacheClient.incrBy(jedisGroup, key, 1);
  
            return val;
        }
    }

    
    
}
