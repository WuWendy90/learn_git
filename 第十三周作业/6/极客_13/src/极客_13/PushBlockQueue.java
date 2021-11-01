package ����_13;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
 
/**
 * һ����Ϣ���м�������ֻҪ��������������Ϣ��������У��ͻ�֪ͨ������ִ�����Ѳ���
 */
public class PushBlockQueue extends LinkedBlockingQueue<Object> {
    //���߳�ִ�У������̳߳�
    private static ExecutorService es = Executors.newFixedThreadPool(10);
    //�����еĶ���ģʽ��ʵ����һ�����е���
    private static PushBlockQueue pbq = new PushBlockQueue();
    //״̬��ʶλ
    private boolean flag = false;
 
    private PushBlockQueue() {
    }
 
    public static PushBlockQueue getInstance() {
        return pbq;
    }
 
    /**
     * ���м�������
     */
    public void start() {
        if (!this.flag) {
            flag = true;
        } else {
            throw new IllegalArgumentException("�����������������ظ�������");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (flag) {
                        //�Ӷ�����ȡ��Ϣ
                        Object obj = take();
                        //�̳߳��ɳ��߳�������ȡ������Ϣ
                        es.execute(new PushBlockQueueHandler(obj));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
 
    /**
     * ֹͣ���м���
     */
    public void stop(){
        this.flag=false;
    }
}
