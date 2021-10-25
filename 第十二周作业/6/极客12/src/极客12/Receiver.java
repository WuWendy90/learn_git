package ����12;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
 
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
 
 
public class Receiver {
	// ConnectionFactory �����ӹ�����JMS ������������
   private static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
           ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
	
   public static void main(String[] args) throws JMSException {
	    // Connection ��JMS �ͻ��˵�JMS Provider ������
		final Connection connection =  connectionFactory.createConnection();
		
		connection.start();
		// Session�� һ�����ͻ������Ϣ���߳�
		final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		// Destination ����Ϣ��Ŀ�ĵ�;��Ϣ��˭�ǻ�ȡ.
		Destination destination =  session.createQueue("my-queue");
		// �����ߣ���Ϣ������
		MessageConsumer consumer1 =  session.createConsumer(destination);
		
		consumer1.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) {
 
					try {
						
						TextMessage message = (TextMessage)msg ;
						System.out.println("consumer1�յ���Ϣ�� "+message.getText());
						session.commit();
					} catch (JMSException e) {				
						e.printStackTrace();
					}
					
				}
			});
	
		// ����һ�������ߣ���Ϣ������
		MessageConsumer consumer2 =  session.createConsumer(destination);
		
		consumer2.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) {
 
					try {
						
						TextMessage message = (TextMessage)msg ;
						System.out.println("consumer2�յ���Ϣ�� "+message.getText());
						session.commit();
					} catch (JMSException e) {				
						e.printStackTrace();
					}
					
				}
			});
 
		
		
	
}
	
	
 
}
