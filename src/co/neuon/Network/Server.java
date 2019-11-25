package co.neuon.Network;

import co.neuon.Game;

import java.nio.ByteBuffer;

public class Server extends Connectable {

    Game game;

    public Server(Game game){
        super(true);
    }

    void onClientConnect(int clientID){
        ByteBuffer buf = ByteBuffer.allocate(12);
        buf.putInt(command.createPlayerObject.val());
        buf.putInt(10);
        buf.putInt(30);
        buf.flip();

//        buf.position(4);
//        game.createCircle(buf);
//        buf.position(0);

        clients.get(clientID).sendData(buf);
    }
    void setClientID(ByteBuffer buf) {

    }
    void createObject(ByteBuffer buf) {

    }
    void createPlayerObject(ByteBuffer buf) {

    }
    void moveObject(Object attachment, ByteBuffer buf) {

    }
    void newClientConnectedID(ByteBuffer buf) {

    }
}
