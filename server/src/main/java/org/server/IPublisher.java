package org.server;

public interface IPublisher {

    void setExchangeTypeAndName(ExchangeType type,String name);
    void closeConnection();
}
