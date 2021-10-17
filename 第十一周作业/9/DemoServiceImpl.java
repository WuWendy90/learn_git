package redis;

public class DemoServiceImpl {

	private StringRedisTemplate stringRedisTemplate;
	 
    public void saveMessageToQueue(String queue, String content){
        stringRedisTemplate.convertAndSend(queue, content);
    }
 
}
