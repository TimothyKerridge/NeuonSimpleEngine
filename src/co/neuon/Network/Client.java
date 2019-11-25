package co.neuon.Network;

import java.nio.ByteBuffer;

public class Client extends Connectable {

    public Client(String ip){
        super(false);
        clientConnect(ip);
    }

    void onClientConnect(int clientID) {

    }
    void setClientID(ByteBuffer buf) {

    }
    void createObject(ByteBuffer buf) {

    }
    void createPlayerObject(ByteBuffer buf) {
        int x = buf.getInt();
        int y = buf.getInt();
        System.out.println(x+":"+y);
    }
    void moveObject(Object attachment, ByteBuffer buf) {

    }
    void newClientConnectedID(ByteBuffer buf) {

    }
}
