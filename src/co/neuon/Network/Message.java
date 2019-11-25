package co.neuon.Network;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class Message{
    private static final int snippitLength = 131071;//131071

    public ByteBuffer header;
    public ByteBuffer fullBuffer;
    LinkedList<ByteBuffer> snips;
    public ByteBuffer currentBuffer;

    public int size;
    public int remaining;
    Type type;

    enum Type{
        read, write
    }

    public Message(int size, Type type){
        this.type = type;
        this.size = size;
        this.remaining = size;

        if(type == Type.read) {
            if (size <= snippitLength) {
                currentBuffer = ByteBuffer.allocate(size);
            } else {
                snips = new LinkedList<>();
                currentBuffer = ByteBuffer.allocate(snippitLength);
                snips.add(currentBuffer);
            }
        }else{
            header = ByteBuffer.allocate(4);
            header.clear();
            header.putInt(size);//numberOfBytes
            header.flip();
        }
    }
    public void update(int bytesRead){
        remaining -= bytesRead;
        if(this.type == Type.read){
            if(remaining != 0) {
                if (!currentBuffer.hasRemaining()) {
                    if (remaining <= snippitLength) {
                        currentBuffer = ByteBuffer.allocate(remaining);
                        snips.add(currentBuffer);
                    } else {
                        currentBuffer = ByteBuffer.allocate(snippitLength);
                        snips.add(currentBuffer);
                    }
                }
            }
        }
    }

    public ByteBuffer getFullBuffer(){
        if(snips == null) return currentBuffer;
        else{
            System.out.println("ERROR Unhandled multi snip reading! (easy to implement)");
            return null;
        }
    }
}