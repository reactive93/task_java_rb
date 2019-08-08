package org.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectorFactory {

    private Channel channel;
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(Sender.class.getName());

    private void _connect(String host,int port,String username, String password) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setPort(port);

        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,
                    "failed connection to " + this.connection.getAddress() + ":"+ this.connection.getPort());
        }
    }

    public void connect(String host) {
        this._connect(host, 5672,"guest","guest");
    }

    public void connect(String host, String username, String password) {
        this._connect(host, 5672, username, password);
    }
    public void connect(String host,int port,String username, String password) {
        this._connect(host, port, username, password);
    }
    public IConnector getConnectorType(ConnectionType type) {
        switch (type) {
            case PUBLISHER: return new Publisher(this.channel, this.connection);
            case QUEUE: return new Sender(this.channel, this.connection);
            default: return new Sender(this.channel, this.connection);
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
