package co.neuon.Network;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public abstract class Connectable {

    Selector selector;

    ServerSocketChannel serverSocketChannel;
    HashMap<Integer, Connection> clients;
    int clientIDCounter;
    boolean isServer;

    SocketChannel clientSocketChannel;
    String remoteAddress = "192.168.0.2";

    int port = 25565;

    public Connectable(boolean isServer){
        this.isServer = isServer;
        try{
            selector = Selector.open();
            if(isServer) startServer();
        }catch(Exception e){e.printStackTrace();}
    }

    public void clientConnect(String ip){
        try {
            this.remoteAddress = ip;
            clientSocketChannel = SocketChannel.open();
            clientSocketChannel.configureBlocking(false);
            clientSocketChannel.connect(new InetSocketAddress(remoteAddress, port));

            Connection connection = new Connection(this, clientSocketChannel);
            SelectionKey key = clientSocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, connection);
        }catch(Exception e){e.printStackTrace();}
    }
    public void startServer(){
        try {
            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            clients = new HashMap<>();
            clientIDCounter = 0;
        }catch (Exception e){e.printStackTrace();}
    }

    public void tick(){
        if(isServer){
            listenForNewConnections();
        }
        checkOpenConnections();
        //serverListening for connections
    }
    public void checkOpenConnections(){
        if(isServer) serverNetwork();
        else clientNetwork();
    }
    public void listenForNewConnections(){
        try {
            SocketChannel channel = serverSocketChannel.accept();

            if (channel != null) {
                channel.configureBlocking(false);

                Connection connection = new Connection(this, channel);
                clientIDCounter++;
                int clientID = clientIDCounter;
                connection.clientID = clientID;
                clients.put(clientID, connection);

                System.out.println("Client [" + clientID + "] Accepted: [" + channel.getRemoteAddress() + "]");

                SelectionKey key = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, connection);

                onClientConnect(clientID);
            }
        }catch(Exception e){e.printStackTrace();}
    }

    public void serverNetwork() {
        try {
            int readyChannels = selector.selectNow();

            if(readyChannels == 0) return;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if(key.isAcceptable()) System.out.println("[Server] Channel Accpetion");
                if (key.isConnectable()) System.out.println("[Server] Channel Connected");
                if (key.isReadable()) ((Connection) key.attachment()).readTick(key);
                if (key.isWritable()) ((Connection)key.attachment()).writeTick();

                keyIterator.remove();
            }
        }catch(Exception e){e.printStackTrace();}
    }

    public void clientNetwork(){
        try {
            if(clientSocketChannel.finishConnect()) {
                int readyChannels = selector.selectNow();

                if (readyChannels == 0) return;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) System.out.println("Channel Accepted");
                    if (key.isConnectable()) System.out.println("Channel Connected");
                    if (key.isReadable()) ((Connection) key.attachment()).readTick(key);
                    if (key.isWritable()) ((Connection) key.attachment()).writeTick();

                    keyIterator.remove();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    enum command{
        setClientID(0),
        createObject(1),
        createPlayerObject(2),
        moveObject(3),
        newClientConnectedID(4);

        private int val;
        command(int val) {
            this.val = val;
        }
        public int val() {
            return val;
        }
    }

    abstract void onClientConnect(int clientID);
    abstract void setClientID(ByteBuffer buf);//0
    abstract void createObject(ByteBuffer buf);//1
    abstract void createPlayerObject(ByteBuffer buf);//2
    abstract void moveObject(Object attachment, ByteBuffer buf);//3
    abstract void newClientConnectedID(ByteBuffer buf);//4

    public void executeCommand(Object attachment, int commandID, ByteBuffer buf){
//        System.out.println("CommandID: "+commandID);
        switch (commandID){
            case(0): setClientID(buf);break;
            case(1): createObject(buf);break;
            case(2): createPlayerObject(buf);break;
            case(3): moveObject(attachment, buf);break;
            case(4): newClientConnectedID(buf);break;
        }
    }

//    public void setClientID(ByteBuffer buf) {
//        if(clientID == 0) {
//            clientID = buf.get();
//            game.frame.setTitle("Client "+clientID);
//            connection.clientID = clientID;
//        }
//    }
//
//    public void createObject(ByteBuffer buf) {
//        game.createCObject(buf);
//    }
//    public void createPlayerObject(ByteBuffer buf) {
//        if(playerObject == null) playerObject = game.createPObject(buf);
//        playerObject.color = Color.green;
//    }
//
//    public void moveObject(Object attachment, ByteBuffer buf) {
//        int id = buf.getInt();
//        int x = buf.getInt();
//        int y = buf.getInt();
//        int xVel = buf.getInt();
//        int yVel = buf.getInt();
//
//        CircleObject object = game.objects.get(id);
//        if(object != null) {
//            object.x = x;
//            object.y = y;
//            object.xVel = xVel;
//            object.yVel = yVel;
//        }
//    }
//
//    public void newClientConnectedID(ByteBuffer buf) {
//        int newClientID = buf.get();
//        System.out.println(clientID+":"+newClientID);
//    }

}
