package ����12;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
 
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
 
public class Sender {
	
	public static void main(String[] args) throws JMSException, InterruptedException {
		// ConnectionFactory �����ӹ�����JMS ������������
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				                                  ActiveMQConnection.DEFAULT_USER,
				                                  ActiveMQConnection.DEFAULT_PASSWORD,
				                                  "tcp://localhost:61616");
		
		// Connection ��JMS �ͻ��˵�JMS Provider ������
		Connection connection =  connectionFactory.createConnection();
		
		connection.start();
		// Session�� һ�����ͻ������Ϣ���߳�
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		
		// Destination ����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
		Destination destination =  session.createQueue("my-queue");
		
		// MessageProducer����Ϣ������
		MessageProducer producer =  session.createProducer(destination);
		
		// ���ò��־û������Ը���
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        
		for(int i=0;i<10;i++){
			//�����ı���Ϣ
			TextMessage message = session.createTextMessage("hello everyone, this is a test message"+i);
			
			Thread.sleep(1000);
			//������Ϣ
			producer.send(message);
		}
		
		session.commit();
		session.close();
		connection.close();
	}
 
}
  