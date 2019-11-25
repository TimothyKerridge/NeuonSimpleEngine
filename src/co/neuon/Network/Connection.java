package co.neuon.Network;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class Connection {

    Connectable connectable;
    public int clientID;

    SocketChannel channel;

    LinkedList<Message> messagesToWrite = new LinkedList<>();
    //    LinkedList<Message> messagesWritten = new LinkedList<>();
    LinkedList<Message> messagesReadyToProcess = new LinkedList<>();

    public Connection(Connectable connectable, SocketChannel channel){
        this.connectable = connectable;
        this.channel = channel;
    }

    public void sendData(ByteBuffer buffer){

//        System.out.print("["+clientID+"]");
        Message message = new Message(buffer.limit(), Message.Type.write);
        message.fullBuffer = buffer;

        messagesToWrite.add(message);
    }

    public void writeTick(){
        if(messagesToWrite.size()>0) {
            write(messagesToWrite.getFirst());
        }

//            for (int i = 0; i < messagesWritten.size(); i++) {
//                messagesToWrite.remove(messagesWritten.get(i));
//            }
//            messagesWritten.clear();
    }

    private void write(Message message){
        try {
            if (message.header.hasRemaining()) {
                int bytesWritten = channel.write(message.header);
//                System.out.println("H:"+message.header.remaining()+":"+clientID+":"+bytesWritten);
            }
            if(message.remaining>0){
                message.fullBuffer.position(0);//todo remove this line..? It is required as I am writing the same buffer multiple times to multiple clients, so it needs to be reset..
                if(message.fullBuffer.position()!=0) System.out.println("[Connection] buffer not ready to be sent!");
                int bytesWritten = channel.write(message.fullBuffer);
//                System.out.println(":"+clientID+":"+bytesWritten);
                message.update(bytesWritten);
            }
            if (message.remaining == 0) {
                messagesToWrite.removeFirst();
//                messagesWritten.add(message);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    Message currentMessageBeingProcessed;
    public void readTick(SelectionKey key){
        read(channel);
        processMessages(key);
    }

    Message currentMessageBeingRecieved;
    private void read(SocketChannel channel){
        try {
            if (currentMessageBeingRecieved == null) { //header
                ByteBuffer header = ByteBuffer.allocate(4);

                int bytesRead = channel.read(header);
                if(bytesRead!=4) {
                    System.out.println(clientID+"] Not enough Bytes read!");
                    return;
                }
                header.flip();
                int size = header.getInt();
//                System.out.println("Read Package size: "+size);
                currentMessageBeingRecieved = new Message(size, Message.Type.read);
            }
            if(currentMessageBeingRecieved != null){ //body
                int bytesRead = channel.read(currentMessageBeingRecieved.currentBuffer);
                currentMessageBeingRecieved.update(bytesRead);
                if (currentMessageBeingRecieved.remaining == 0) {
                    currentMessageBeingRecieved.currentBuffer.flip();
                    messagesReadyToProcess.add(currentMessageBeingRecieved);
                    currentMessageBeingRecieved = null;
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }

    public void processMessages(SelectionKey key){
        LinkedList<Message> messages = ((Connection) key.attachment()).messagesReadyToProcess;
        for(int i = 0; i < messages.size(); i++){
            currentMessageBeingProcessed = messages.get(i);
            ByteBuffer buf = currentMessageBeingProcessed.getFullBuffer();
            int commandID = buf.getInt();

            connectable.executeCommand(key.attachment(), commandID, buf);
        }
        ((Connection) key.attachment()).messagesReadyToProcess.clear();
    }
}
