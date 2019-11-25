package co.neuon.Objects;

import co.neuon.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;

public class Circle extends GameObject {

    int radius;
    Color color;


    public Circle(Window window, ByteBuffer buf){
        super(window);
//        objectID = buf.getInt();
        radius = 20;
        setX(buf.getInt());
        setY(buf.getInt());
    }
//    public Circle(co.neuon.BasicRenderEngine.Window window, int radius, int x, int y){
//        super(window);
//        this.radius = radius;
//        setX(x);
//        setY(y);
//    }
//    public Circle(Window window, int radius, int x, int y, Color color){
//        super(window);
//        this.radius = radius;
//        setX(x);
//        setY(y);
//        this.color = color;
//    }

    @Override
    public void tick() {

        //basic movement
        double speed = 200;
        double val = window.deltaTime();
        if(window.held(KeyEvent.VK_RIGHT)) deltaX((float)(speed*val));
        if(window.held(KeyEvent.VK_LEFT))  deltaX((float)(-speed*val));
        if(window.held(KeyEvent.VK_UP))    deltaY((float)(-speed*val));
        if(window.held(KeyEvent.VK_DOWN))  deltaY((float)(speed*val));

        //bounds check
        if(x() < 0) setX(0);
        if(x() > window.getWidth()-radius) setX(window.getWidth()-radius);
        if(y() < 0) setY(0);
        if(y() > window.getHeight()-radius) setY(window.getHeight()-radius);

    }

    @Override
    public void render(Graphics g) {
        if(color == null) g.setColor(Color.white);
        else g.setColor(color);
        g.drawOval(x(), y(), radius, radius);
    }
}
