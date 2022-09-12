package datacollectiondispatcher.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;

import javax.jms.*;
// Klasa Consumer ka si qellim te marre mesazhe nga activemq. Consumer i ben implement runnable ne menyre qe te
// behet run si Thread me vete. Metoda "consume" merr mesazhin nga queue e specifikuar te attributi "queueName"
// dhe i ben return asaj vlere. Te metoda "run" qe merret nga "Runnable" thirret metoda consume dhe return value (mesazhi i marre nga activemq)
// ruhet te property "msg". Me ane te metodes getMsg() mesazhi mund te merret nga nje klase e jashtme.
// Kjo si klase perdoret vetem te klasa "MessageHandler"
public class Consumer implements Runnable {
    private final String brokerUrl; //variable q inicializohet me linkun e server
    private final String queueName; // variable q inicializohet me emrin e queue nga merret mesazho
    private String msg; // variable ku ruhet mesazhi pasi merret nga queue specifik
    public Consumer(String url,String queue){ // Constructor i klases, inicializohet te MessageHandler
        this.brokerUrl = url;
        this.queueName = queue;
    }

    private String consume(){  //metoda consume merr mesazhin nga queue specifkik dhe i ben return
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            ActiveMQPrefetchPolicy policy = new ActiveMQPrefetchPolicy();
            policy.setAll(0);
            factory.setPrefetchPolicy(policy);
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
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
    } // metoda run eshte metoda qe thirret kur nje objekt i kesaj klase, kthehet ne thread. Ne kete rast mesazhi qe merret nga queue specifik ruhet te attributi msg

    public String getMsg() {
        return msg;
    } // kjo eshte nje get metode per atributin msg. kjo metode perdoret pasi maesazhi i marre nga metoda consume eshte ruajtur brenda variables msg.
}
