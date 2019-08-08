package org.server;

import java.util.Random;

public class Worker implements Runnable {

    private IConnector connector;

    public Worker(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public void run() {
        while (true) {
            Random random = new Random();
            String message = "Rate of Dollar " + random.nextInt(100);
            this.connector.sendMessage(message,"");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
