package co.neuon;

public class Key {

    private boolean state, lastState, pressed, released;

    public void press(){
        state = true;
        if(state != lastState){
            pressed = true;
        }
        lastState = state;
    }
    public void release(){
        state = false;
        if(state != lastState){
            released = true;
        }
        lastState = state;
    }
    public void reset(){
        pressed = false;
        released = false;
    }

    public boolean isPressed(){
        return pressed;
    }
    public boolean isReleased(){
        return released;
    }
    public boolean isHeld(){
        return state;
    }

}
