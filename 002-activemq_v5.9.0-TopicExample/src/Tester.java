import java.io.IOException;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.jndi.ActiveMQInitialContextFactory;

public class Tester{
	
	public static void main(String[] args) throws NamingException, IOException, JMSException {
		
		Context ctx = setContextDetails();
		TopicConnectionFactory tcf = (TopicConnectionFactory) ctx.lookup("ConnectionFactory");
		
		TopicConnection tc = (TopicConnection) tcf.createConnection("admin", "admin");
		
		TopicSession ts = tc.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		tc.start();
		
		Topic t = (Topic) ctx.lookup("casualties");
		
		ts.createSubscriber(t).setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message m) {
				if(m instanceof TextMessage){
					TextMessage msg = (TextMessage) m;
					try {
						System.out.println(msg.getText());
					} catch (JMSException e) {e.printStackTrace();}
				}
			}
		});
		
		/*sendMessage("lols", ts, t);
		ts.close();
		tc.stop();
		tc.close();*/
		
		/*
		System.out.println( receiveMessage(ts, t) );
		ts.close();
		tc.stop();
		tc.close();
		*/
		
	}
	public static void sendMessage(String msg, TopicSession session, Topic queue) throws JMSException{
		TopicPublisher producer = (TopicPublisher) session.createPublisher(queue);
		TextMessage tm = session.createTextMessage();
		tm.setText(msg);
		//producer.send(tm); // Send is the same as publish. See http://docs.oracle.com/javaee/1.4/api/javax/jms/TopicPublisher.html
		producer.publish(tm);
		producer.close();
	}
	public static String receiveMessage(TopicSession session, Topic topic) throws JMSException{
		MessageConsumer consumer = session.createConsumer(topic);
		Message r = consumer.receive(); // Waits until a message arrives, throw java.io.EOFException if the message queue is turned off
		if(r instanceof TextMessage){
			TextMessage msg = (TextMessage) r;
			return msg.getText();
		}
		return null;
	}
	
	public static Context setContextDetails() throws NamingException, IOException{
		Properties props = new Properties();
		props.put(Context.PROVIDER_URL, "tcp://localhost:61616");
		props.put(Context.INITIAL_CONTEXT_FACTORY, ActiveMQInitialContextFactory.class.getName()); //"org.apache.activemq.jndi.ActiveMQInitialContextFactory"
		props.put("topic.casualties", "casualties");
		Context ctx = new InitialContext(props);
		return ctx;
	}
	
}
