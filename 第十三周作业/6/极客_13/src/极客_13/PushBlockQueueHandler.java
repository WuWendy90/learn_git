package ����_13;

public class PushBlockQueueHandler {

	/**
	 *�൱�ڶ�����Ϣ��������
	 */
	public class PushBlockQueueHandler implements Runnable{
	    //���ѵĶ���
	    private Object obj;
	 
	    public PushBlockQueueHandler(Object obj){
	        this.obj=obj;
	    }
	    //�����߳�
	    @Override
	    public void run() {
	        doBusiness();
	    }
	    //������Ϊ
	    private void doBusiness() {
	        System.out.println(Thread.currentThread().getName()+"-�յ���Ϣ��"+obj);
	    }
	}
	
}
