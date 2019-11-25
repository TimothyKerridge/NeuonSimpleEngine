package co.neuon.Objects;


import co.neuon.Window;

import java.awt.*;

public class Cursor extends GameObject {

    Circle circle;
    int radius = 20;

    public Cursor(Window window){
        super(window);
//        circle = new Circle(window, radius, 0, 0);
    }

    @Override
    public void tick() {
        circle.setX(window.getMouseX()-radius/2);
        circle.setY(window.getMouseY()-radius/2);
    }

    @Override
    public void render(Graphics g) {
        circle.render(g);
    }
}
