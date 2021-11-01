package 极客_13;

public class PushBlockQueueHandler {

	/**
	 *相当于队列消息的消费者
	 */
	public class PushBlockQueueHandler implements Runnable{
	    //消费的对象
	    private Object obj;
	 
	    public PushBlockQueueHandler(Object obj){
	        this.obj=obj;
	    }
	    //消费线程
	    @Override
	    public void run() {
	        doBusiness();
	    }
	    //消费行为
	    private void doBusiness() {
	        System.out.println(Thread.currentThread().getName()+"-收到消息："+obj);
	    }
	}
	
}
