package datacollectiondispatcher.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;

import javax.jms.*;

// Klasa Producer perdoret per te cuar nje mesazh me ane te activemq. Producer i ben implement "Runnable"
// ne menyre qe te mund te behet run si thread me vete. Kur nje objekt Producer inicializohet, kerkon tre
// parametra: 1.brokerURL - linku i serverit te activemq, 2.queueName - emr i queue ku duhet te cohet mesazhi
// 3. message - mesazhi qe duhet te cohet. Producer perdoret te klasa "MessageHandler".
public class Producer implements Runnable{
    private final String brokerUrl;// linku i  server activemq
    private final String queueName;// emr i queue ku cohet mesazhi
    private final String message;//mesazhi qe duhet te cohet
    public Producer(String url,String queue,String msg){ // construtor
        this.brokerUrl = url;
        this.queueName = queue;
        this.message = msg;
    }
    @Override
    public void run() { // ketu dergimim i mesazhit ndodh direkte ne metoden run
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(this.brokerUrl);

            ActiveMQPrefetchPolicy policy = new ActiveMQPrefetchPolicy();
            policy.setAll(0); // heq mundesin per prefetch te mesazheve
            factory.setPrefetchPolicy(policy);

            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(this.queueName);

            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // non_persistent dmth q mesazhet nuk ruhen ne hard drive, ne momentin qe serveri fiket ato mesazhe qe jane akoma ne queue fshihen

            producer.send(session.createTextMessage(this.message)); // ktu drg mesazhi

            producer.close();
            session.close();
            connection.close();
        }catch (JMSException e){
            System.out.println("Messaging problem!");
        }
    }
}

