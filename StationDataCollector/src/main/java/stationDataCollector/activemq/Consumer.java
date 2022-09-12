package stationDataCollector.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;

import javax.jms.*;
import java.util.concurrent.Callable;

public class Consumer implements Runnable {
    private final String brokerUrl;
    private final String queueName;
    private String msg;
    public Consumer(String url,String queue){
        this.brokerUrl = url;
        this.queueName = queue;
    }

    private String consume(){
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            ActiveMQPrefetchPolicy policy = new ActiveMQPrefetchPolicy();
            policy.setAll(0);
            factory.setPrefetchPolicy(policy);

            //Create Connection
            Connection connection = factory.createConnection();

            // Start the connection
            connection.start();

            // Create Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Create queue
            Destination queue = session.createQueue(queueName);

            MessageConsumer consumer = session.createConsumer(queue);

            Message message = consumer.receive(1000);

            String text = "";

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                text = textMessage.getText();
            }
            consumer.close();
            session.close();
            connection.close();
            return text;

        } catch (Exception ex) {
            return "error";
        }
    }
    @Override
    public void run() {
        this.msg = consume();
    }

    public String getMsg() {
        return msg;
    }
}
