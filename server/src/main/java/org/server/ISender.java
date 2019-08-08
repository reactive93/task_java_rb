package org.server;

public interface ISender {

    void setQueue(String queue);
    void closeConnection();

}
