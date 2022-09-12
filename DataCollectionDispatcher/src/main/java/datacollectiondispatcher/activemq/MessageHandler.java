package datacollectiondispatcher.activemq;

import java.util.ArrayList;
import java.util.List;

// MessageHandler eshte klase qe merret me te gjitha komunikimet me activemq server.

public class MessageHandler {
    private static final String brokerURL = "tcp://localhost:61616";

    // metoda sendMessage perdor nje objekt producer per te cuar nje mesazh nepermjet activemq
    // objekti producer perdoret per te krijuar nje thread, dhe ai thread behet run.
    public static void sendMessage(String queue,String msg){
        Producer producer = new Producer(brokerURL,queue,msg);
        Thread producerThread = new Thread(producer);
        producerThread.start();
    }

    // metoda getMessage merr mesazh nga activemq nepermjet nje objekti cosumer
    // objekti consumer perdoret per te krijuar nje thread, dhe ai thread behet run.
    public static String getMessage(String queue){
        Consumer consumer = new Consumer(brokerURL,queue); // inicializim i objektit consumer
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        try {
            consumerThread.join(); // metoda join perdoret qe mesazhi te ruhet te attributi "msg" i objektit consumer
            String msg =  consumer.getMsg(); // metoda getMsg() perdoret per te marre mesazhin e rajtur te attributi "msg" i objektit consumer
            return msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "error";
    }
    public static List<String> getAllMessages(String queue){ // kjo metode merr gjithe mesazhet nga nje queue
        List<String> messages = new ArrayList<String>(); // lista e mesazheve qe do te behet return
        String message = "";
        do{ //metoda e mesiperme getMessage() thirret derisa nuk ka me mesazhe
            message = getMessage(queue);
            messages.add(message); // mesazhi qe merret futet te lista e mesazheve qe do behet return
        }while (!message.isBlank()); // loop vazhdon deri ne momentin qe nuk gjen me mesazh ne queue
        messages.remove(messages.size()-1); // mesazhi i fundit eshte gjitmone bosh, sepse do{}while() lejon nje tek mesazh bosh. Ketu mesazhi i fundit bosh hiqet nga lista qe do behet return
        return messages; //return i lites se mesazheve
    }
}


