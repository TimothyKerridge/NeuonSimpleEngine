package co.neuon.Objects;


import co.neuon.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Rect extends GameObject {

    int width, height;
    Color color;

    public Rect(Window window, int width, int height, int x, int y){
        super(window);
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }


    @Override
    public void tick() {
    }
    @Override
    public void render(Graphics g) {
        if(color == null) g.setColor(Color.white);
        else g.setColor(color);
        g.drawRect(x(), y(), width, height);
    }
}
