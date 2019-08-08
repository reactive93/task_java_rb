package org.app;

import org.client.ExchangeType;
import org.client.Receiver;
public class Application {

    public static void main(String[] args) {


        String host = "localhost";

        Receiver receiver = new Receiver();
        receiver.connect(host);
        receiver.setExchange("my_fanout", ExchangeType.FANOUT,"todo");
        Thread receiver_thread = new Thread(receiver);
        receiver_thread.setDaemon(true);
        receiver_thread.start();
    }
}
