package 极客_13;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
 
/**
 * 一个消息队列监听器，只要生产者生产出消息并推入队列，就会通知处理器执行消费操作
 */
public class PushBlockQueue extends LinkedBlockingQueue<Object> {
    //多线程执行，采用线程池
    private static ExecutorService es = Executors.newFixedThreadPool(10);
    //单例中的饿汉模式，实例化一个队列单例
    private static PushBlockQueue pbq = new PushBlockQueue();
    //状态标识位
    private boolean flag = false;
 
    private PushBlockQueue() {
    }
 
    public static PushBlockQueue getInstance() {
        return pbq;
    }
 
    /**
     * 队列监听启动
     */
    public void start() {
        if (!this.flag) {
            flag = true;
        } else {
            throw new IllegalArgumentException("队列已启动，不可重复启动！");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (flag) {
                        //从队列中取消息
                        Object obj = take();
                        //线程池派出线程来消费取出的消息
                        es.execute(new PushBlockQueueHandler(obj));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
 
    /**
     * 停止队列监听
     */
    public void stop(){
        this.flag=false;
    }
}
