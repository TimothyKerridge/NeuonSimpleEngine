package co.neuon.Objects;


import co.neuon.Window;

import java.awt.*;

public abstract class GameObject {

    Window window;

    private int objectID;
    float x, y;

    public GameObject(Window window){
        this.window = window;
        objectID = window.getNewObjectID();
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public void render(){
        window.render(this);
    }

    public int getID(){
        return objectID;
    }

    public int x(){
        return (int)x;
    }
    public int y(){
        return (int)y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
    double a = 123.0;



    public void setY(float y) {
        this.y = y;
    }
    public void deltaX(float x){
        this.x += x;
    }
    public void deltaY(float y){
        this.y += y;
    }
}
