package redis;

public class Demo {

	private DemoService demoService;
	 
	   public void pushMessage(String msg){
	       log.info("���յ���Ϣ����{}��", msg);
	       
	       demoService.saveMessageToQueue("handle_message_queue_prefix1", msg);
}
