package org.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Publisher implements IConnector, IPublisher {

    private Channel channel;
    private Connection connection;
    private String exchangeName;
    private static final Logger LOGGER = Logger.getLogger(Sender.class.getName());

    public Publisher(Channel channel, Connection connection) {
        this.channel = channel;
        this.connection = connection;
    }


    @Override
    public void setExchangeTypeAndName(ExchangeType type, String exchangeName) {
        this.exchangeName = exchangeName;
        try {
            this.channel.exchangeDeclare(exchangeName, type.name().toLowerCase());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"cannot exchange declare");
        }
    }


    @Override
    public void sendMessage(String message,String key) {
        try {
            channel.basicPublish(this.exchangeName, key, null, message.getBytes("UTF-8"));
            System.out.println("Send message -> " + message);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"cannot getBytes from message");
        }
    }

    @Override
    public void closeConnection() {
        try {
            this.connection.close();
            this.channel.close();
        } catch (IOException | TimeoutException e) {
            LOGGER.log(Level.WARNING,"close connection ");
        }
    }
}
