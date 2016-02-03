import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.jndi.ActiveMQInitialContextFactory;

public class Tester {
	
	public static void main(String[] args) throws NamingException, JMSException, IOException {
		
		Context ctx = setContextDetails();
		QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
		
		QueueConnection qc = qcf.createQueueConnection("publisher", "password");
		
		QueueSession qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		qc.start();
		
		//Queue q = (Queue)ctx.lookup("dynamicQueues/casualties");
		//Queue q = qs.createQueue("casualties");
		Queue q = (Queue) ctx.lookup("casualties");
		
		//sendMessage("Ai jasus", qs, q);
		System.out.println( receiveMessage(qs, q) );
		
		qs.close();
		
		qc.stop();
		qc.close();
		
	}
	
	public static void sendMessage(String msg, QueueSession session, Queue queue) throws JMSException{
		MessageProducer producer = (MessageProducer) session.createProducer(queue);
		TextMessage tm = session.createTextMessage();
		tm.setText(msg);
		producer.send(tm);
		producer.close();
	}
	
	public static String receiveMessage(QueueSession session, Queue queue) throws JMSException{
		MessageConsumer consumer = session.createConsumer(queue);
		Message r = consumer.receive(); // Waits until a message arrives, throw java.io.EOFException if the message queue is turned off
		if(r instanceof TextMessage){
			TextMessage msg = (TextMessage) r;
			return msg.getText();
		}
		return null;
	}
	
	public static Context setContextDetails() throws NamingException, IOException{
		/*
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		InputStream is = Object.class.getResourceAsStream("/my.jndi.properties");
		props.load(is);
		*/
		Properties props = new Properties();
		props.put(Context.PROVIDER_URL, "tcp://localhost:61616");
		props.put(Context.INITIAL_CONTEXT_FACTORY, ActiveMQInitialContextFactory.class.getName()); //"org.apache.activemq.jndi.ActiveMQInitialContextFactory"
		props.put("queue.casualties", "casualties");
		Context ctx = new InitialContext(props);
		return ctx;
	}
	
}
