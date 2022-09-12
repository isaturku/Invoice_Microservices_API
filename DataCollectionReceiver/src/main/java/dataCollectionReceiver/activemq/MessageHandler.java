package dataCollectionReceiver.activemq;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    private static final String brokerURL = "tcp://localhost:61616";
    public static void sendMessage(String queue,String msg){
        Producer producer = new Producer(brokerURL,queue,msg);
        Thread producerThread = new Thread(producer);
        producerThread.start();
    }
    public static String getMessage(String queue){
        Consumer consumer = new Consumer(brokerURL,queue);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        try {
            consumerThread.join();
            String msg =  consumer.getMsg();
            return msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "error";
    }
    public static List<String> getAllMessages(String queue){
        List<String> messages = new ArrayList<String>();
        String message = "msg";
        do{
            message = getMessage(queue);
            messages.add(message);
        }while (!message.isBlank());
        messages.remove(messages.size()-1);
        return messages;
    }
}

