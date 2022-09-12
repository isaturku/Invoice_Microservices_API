package dataCollectionReceiver.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;

import javax.jms.*;

public class Producer implements Runnable{
    private final String brokerUrl;
    private final String queueName;
    private final String message;
    public Producer(String url,String queue,String msg){
        this.brokerUrl = url;
        this.queueName = queue;
        this.message = msg;
    }
    @Override
    public void run() {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(this.brokerUrl);

            ActiveMQPrefetchPolicy policy = new ActiveMQPrefetchPolicy();
            policy.setAll(0);
            factory.setPrefetchPolicy(policy);

            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(this.queueName);

            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            producer.send(session.createTextMessage(this.message));

            producer.close();
            session.close();
            connection.close();
        }catch (JMSException e){
            System.out.println("Messaging problem!");
        }
    }
}

