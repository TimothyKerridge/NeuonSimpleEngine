package co.neuon;


import co.neuon.Objects.*;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class Game {

    Window window;

    HashMap<Integer, GameObject> gameObjects = new HashMap<>();

    public Game(Window window){
        this.window = window;
    }

    public void load(Executable e){
        gameObjects.clear();
        e.execute();
    }

    public void deleteObject(int id){
        gameObjects.remove(id);
    }
    public Rect createRect(int width, int height, int x, int y){
        Rect rect = new Rect(window, width, height, x, y);
        gameObjects.put(rect.getID(), rect);
        return rect;
    }
    public Rect createPlatform(int width, int height, int x, int y){
        Rect rect = new Platform(window, width, height, x, y);
        gameObjects.put(rect.getID(), rect);
        return rect;
    }
    public Rect createPlayer(int width, int height, int x, int y){
        Rect rect = new Player(window, width, height, x, y);
        gameObjects.put(rect.getID(), rect);
        return rect;
    }

    public Text createText(String string, int x, int y){
        Text text = new Text(window, string, x, y);
        gameObjects.put(text.getID(), text);
        return text;
    }
    public Circle createCircle(ByteBuffer buf){
        Circle circle = new Circle(window, buf);
        gameObjects.put(circle.getID(), circle);
        return circle;
    }
    public Cursor createCursor(){
        Cursor cursor = new Cursor(window);
        gameObjects.put(cursor.getID(), cursor);
        return cursor;
    }

    public void tick(){
        Object[] objects = gameObjects.values().toArray();
        for (int i = 0; i < objects.length; i++) {
            ((GameObject) objects[i]).tick();
        }
    }

    public void render(){
        Object[] objects = gameObjects.values().toArray();
        for (int i = 0; i < objects.length; i++) {
            ((GameObject) objects[i]).render();
        }
        window.update();
    }

}
