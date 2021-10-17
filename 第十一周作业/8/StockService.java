package redis;


 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;


public class StockService {

	
	Logger logger = LoggerFactory.getLogger(StockService.class);
	 
    /**
     * ��滹δ��ʼ��
     */
    public static final long UNINITIALIZED_STOCK = -3L;
 
    /**
     * Redis �ͻ���
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
 
    /**
     * ִ�пۿ��Ľű�
     */
    public static final String STOCK_LUA;
 
    static {
        /**
         *
         * @desc �ۼ����Lua�ű�
         * ��棨stock��-1����ʾ���޿��
         * ��棨stock��0����ʾû�п��
         * ��棨stock������0����ʾʣ����
         *
         * @params ���key
         * @return
         *      -3:���δ��ʼ��
         * 		-2:��治��
         * 		-1:���޿��
         * 		���ڵ���0:ʣ���棨�ۼ�֮��ʣ��Ŀ�棩,ֱ�ӷ���-1
         */
        StringBuilder sb = new StringBuilder();
        // exists �ж��Ƿ����KEY��������ڷ���1�������ڷ���0
        sb.append("if (redis.call('exists', KEYS[1]) == 1) then");
        // get ��ȡKEY�Ļ���ֵ��tonumber ��redis����ת�� lua ������
        sb.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        sb.append("    local num = tonumber(ARGV[1]);");
        // ����õ��Ļ��������� -1���������Ʒ��������޵ģ�ֱ�ӷ���1
        sb.append("    if (stock == -1) then");
        sb.append("        return -1;");
        sb.append("    end;");
        // incrby ���Խ��п��Ŀۼ�
        sb.append("    if (stock >= num) then");
        sb.append("        return redis.call('incrby', KEYS[1], 0-num);");
        sb.append("    end;");
        sb.append("    return -2;");
        sb.append("end;");
        sb.append("return -3;");
        STOCK_LUA = sb.toString();
    }
 
    /**
     * @param key           ���key
     * @param expire        �����Чʱ��,��λ��
     * @param num           �ۼ�����
     * @param stockCallback ��ʼ�����ص�����
     * @return -2:��治��; -1:���޿��; ���ڵ���0:�ۼ����֮���ʣ����
     */
    public long stock(String key, long expire, int num, IStockCallback stockCallback) {
        long stock = stock(key, num);
        // ��ʼ�����
        if (stock == UNINITIALIZED_STOCK) {
            RedisLock redisLock = new RedisLock(redisTemplate, key);
            try {
                // ��ȡ��
                if (redisLock.tryLock()) {
                    // ˫����֤�����Ⲣ��ʱ�ظ���Դ�����ݿ�
                    stock = stock(key, num);
                    if (stock == UNINITIALIZED_STOCK) {
                        // ��ȡ��ʼ�����
                        final int initStock = stockCallback.getStock();
                        // ��������õ�redis
                        redisTemplate.opsForValue().set(key, initStock, expire, TimeUnit.SECONDS);
                        // ��һ�οۿ��Ĳ���
                        stock = stock(key, num);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                redisLock.unlock();
            }
 
        }
        return stock;
    }
 
    /**
     * �ۿ��
     *
     * @param key ���key
     * @param num �ۼ��������
     * @return �ۼ�֮��ʣ��Ŀ�桾-3:���δ��ʼ��; -2:��治��; -1:���޿��; ���ڵ���0:�ۼ����֮���ʣ���桿
     */
    private Long stock(String key, int num) {
        // �ű����KEYS����
        List<String> keys = new ArrayList<>();
        keys.add(key);
        // �ű����ARGV����
        List<String> args = new ArrayList<>();
        args.add(Integer.toString(num));
 
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // ��Ⱥģʽ�͵���ģʽ��Ȼִ�нű��ķ���һ��������û�й�ͬ�Ľӿڣ�����ֻ�ֿܷ�ִ��
                // ��Ⱥģʽ
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(STOCK_LUA, keys, args);
                }
 
                // ����ģʽ
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(STOCK_LUA, keys, args);
                }
                return UNINITIALIZED_STOCK;
            }
        });
        return result;
    }
 
 
 
/**
     * �ӿ��(��ԭ���)
     *
     * @param key    ���key
     * @param num    �������
     * @return
     */
    public long addStock(String key, int num) {
 
        return addStock(key, null, num);
    }
 
    /**
     * �ӿ��
     *
     * @param key    ���key
     * @param expire ����ʱ�䣨�룩
     * @param num    �������
     * @return
     */
    public long addStock(String key, Long expire, int num) {
        boolean hasKey = redisTemplate.hasKey(key);
        // �ж�key�Ƿ���ڣ����ھ�ֱ�Ӹ���
        if (hasKey) {
            return redisTemplate.opsForValue().increment(key, num);
        }
 
        Assert.notNull(expire,"��ʼ�����ʧ�ܣ�������ʱ�䲻��Ϊnull");
        RedisLock redisLock = new RedisLock(redisTemplate, key);
        try {
            if (redisLock.tryLock()) {
                // ��ȡ�������ٴ��ж�һ���Ƿ���key
                hasKey = redisTemplate.hasKey(key);
                if (!hasKey) {
                    // ��ʼ�����
                    redisTemplate.opsForValue().set(key, num, expire, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            redisLock.unlock();
        }
 
        return num;
    }
 
    /**
     * ��ȡ���
     *
     * @param key ���key
     * @return -1:���޿��; ���ڵ���0:ʣ����
     */
    public int getStock(String key) {
        Integer stock = (Integer) redisTemplate.opsForValue().get(key);
        return stock == null ? -1 : stock;
    }


	
}
