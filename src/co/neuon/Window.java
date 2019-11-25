package co.neuon;

import co.neuon.Objects.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;

import static java.awt.event.KeyEvent.*;

public class Window extends JComponent {

    public static LinkedList<Window> windows = new LinkedList<>();
    public static Window getWindow(int windowID){
        return windows.get(windowID);
    }

    public Main main;
    JFrame frame;
    int screenWidth = 960, screenHeight = 540;

    LinkedList<GameObject> objectsToRender = new LinkedList<>();
    HashMap<Integer, Key> keys = new HashMap<>();

    private int objectIDCounter = 0;
    public int getNewObjectID(){
        objectIDCounter++;
        return objectIDCounter;
    }

    public Window(Main main){
        this.main = main;
        windows.add(this);
        createWindow();
    }

    int mouseX, mouseY;
    Key mouse1;
    public int getMouseX(){
        return mouseX;
    }
    public int getMouseY(){
        return mouseY;
    }
    public void initKeys(){
        keys.put(VK_LEFT, new Key());
        keys.put(VK_RIGHT, new Key());
        keys.put(VK_UP, new Key());
        keys.put(VK_DOWN, new Key());
        keys.put(VK_BACK_SPACE, new Key());
        mouse1 = new Key();
        keys.put(-1, mouse1);//Mouse1
    }
    public void createWindow(){
        frame = new JFrame("Simple Colab Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        frame.setLocation(760, 0);
        setDoubleBuffered(true);
        frame.add(this);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                main.shouldRun = false;
            }
        });
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouse1.press();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouse1.release();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        initKeys();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch ((int)e.getKeyChar()){
                    case(8): main.input.removeLastCharacter();break;
                    case(10): main.connect.execute(); break;//enter
                    case(9): break;//esc
                    default: main.input.addCharacter(e.getKeyChar());
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                Key key = keys.get(e.getKeyCode());
                if(key != null){
                    key.press();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Key key = keys.get(e.getKeyCode());
                if(key != null){
                    key.release();
                }
            }
        });
        frame.setVisible(true);
    }

    public void tick(){
        if(this.getMousePosition() != null) {
            mouseX = this.getMousePosition().x;
            mouseY = this.getMousePosition().y;
        }
    }

    public boolean pressed(int id){
        return keys.get(id).isPressed();
    }
    public boolean released(int id){
        return keys.get(id).isReleased();
    }
    public boolean held(int id){
        return keys.get(id).isHeld();
    }

    public double deltaTime(){
        return main.deltaTime();
    }

    public void paint(Graphics g) {
        //Reset Screen
        g.setColor(Color.black);
        g.fillRect(0, 0, screenWidth, screenHeight);

        int numberOfItemsToRender = objectsToRender.size();
        for(int i = 0; i < numberOfItemsToRender; i++){
            GameObject object = objectsToRender.getFirst();
            object.render(g);
            objectsToRender.removeFirst();
        }
    }
    public void render(GameObject object){
        //Make object ready to be displayed.
        objectsToRender.add(object);
    }
    public void update(){
        //Draw to window
        repaint();

        //Reset press and released states
        Object[] objects = keys.values().toArray();
        for (int i = 0; i < objects.length; i++) {
            ((Key) objects[i]).reset();
        }
    }
}
