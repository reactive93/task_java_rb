package org.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender implements IConnector, ISender {

    private Channel channel;
    private Connection connection;
    private String queue;
    private static final Logger LOGGER = Logger.getLogger(Sender.class.getName());

    public Sender(Channel channel, Connection connection) {
        this.channel = channel;
        this.connection = connection;
    }

    public void setQueue(String name_queue) {
        this.queue = name_queue;
        try {

            channel.queueDeclare(name_queue,false,false,false,null);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"cannot declare queue -> " + name_queue);
        }
    }


    public void sendMessage(String message, String key) {
        try {
            this.channel.basicPublish("",this.queue, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            System.out.println(message);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Cannot set to queue -> " + this.queue);
        }
    }

    public void closeConnection() {
        try {
            this.connection.close();
            this.channel.close();
        } catch (IOException | TimeoutException e) {
            LOGGER.log(Level.WARNING,"close connection ");
        }

    }
}
