package co.neuon.Objects;

import co.neuon.Window;

import java.util.LinkedList;

public class Platform extends Rect {

    public static LinkedList<Platform> platformList = new LinkedList<>();

    public Platform(Window window, int width, int height, int x, int y) {
        super(window, width, height, x, y);
        platformList.add(this);
    }
}
