package org.app;

import org.server.*;

public class Application {

    public static void main(String[] args) {

        String host = "localhost";
        ConnectorFactory connectionFactory = new ConnectorFactory();
        connectionFactory.connect(host);
        Publisher service = (Publisher) connectionFactory.getConnectorType(ConnectionType.PUBLISHER);
        service.setExchangeTypeAndName(ExchangeType.FANOUT, "my_fanout");
        Worker worker = new Worker(service);

        Thread sender = new Thread(worker);
        sender.setDaemon(true);
        sender.start();

    }
}
