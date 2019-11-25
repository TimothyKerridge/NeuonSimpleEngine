package co.neuon.Objects;


import co.neuon.Window;

import java.awt.*;

public class Text extends GameObject {

    String text;
    Color color;
    int size = 12;

    public Text(Window window, String text, int x, int y) {
        super(window);
        this.text = text;
        setX(x);
        setY(y);
    }
    public Text(Window window, String text, int x, int y, Color color) {
        super(window);
        this.text = text;
        setX(x);
        setY(y);
        this.color = color;
    }
    public Text(Window window, String text, int x, int y, Color color, int size) {
        super(window);
        this.text = text;
        setX(x);
        setY(y);
        this.color = color;
        this.size = size;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g){
        if(color == null) g.setColor(Color.white);
        else g.setColor(color);
        g.setFont(new Font("SanSerif", Font.PLAIN, size));
        g.drawString(text, x(), y());
    }

    public String getText(){
        return text;
    }
    public void setText(String string){
        this.text = string;
    }
    public void addCharacter(Character cha){
        this.text += cha;
    }

    public void removeLastCharacter(){
        if(text.length()>0) this.text = text.substring(0,text.length()-1);
    }

}
