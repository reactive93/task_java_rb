package org.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receiver implements Runnable {

    private Channel channel;
    private Connection connection;
    private String queue;
    private static final Logger LOGGER = Logger.getLogger(Receiver.class.getName());

    public void connect(String host) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            this.channel.basicQos(1);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,
                    "failed connection to " + this.connection.getAddress() + ":"+ this.connection.getPort());
        }
    }

    public void setExchange(String name, ExchangeType type,String ...keys) {
        try {
            this.queue = this.channel.queueDeclare().getQueue();
            this.channel.exchangeDeclare(name, type.name().toLowerCase());
            if (keys != null) {
                for (String key:keys) {
                    channel.queueBind(this.queue, name, key);

                }
            }
            else {
                this.channel.exchangeDeclare(name, type.name().toLowerCase());
                this.queue = this.channel.queueDeclare().getQueue();
                channel.queueBind(this.queue, name, "");
            }

        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"cannot declare queue");
        }
    }


    public void setQueue(String name_queue) {
        this.queue = name_queue;
        try {
            channel.queueDeclare(name_queue,false,false,false,null);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"cannot declare queue -> " + name_queue);
        }
    }

    public void listenQueue() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" Received " + message );
        };
        try {
            channel.basicConsume(this.queue, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Cannot consume");
        }
    }

    @Override
    public void run() {
        this.listenQueue();
    }
}
