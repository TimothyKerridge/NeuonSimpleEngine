package co.neuon.Objects;

import co.neuon.Window;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Player extends Rect {

    float lastX, lastY;

    public Player(Window window, int width, int height, int x, int y){
        super(window, width, height, x, y);
    }

    Double velocityY = 0.00, accelerationY = 1.0;
    boolean isGrounded = false;
    int movementMulti = 10;
    long jumpCounter;

    @Override
    public void tick(){
        movement();

        lastX = x;
        lastY = y;
    }

    private void movement(){
        boolean jump = window.held(KeyEvent.VK_UP);
        boolean jump2 = window.released(KeyEvent.VK_UP);
        boolean left = window.held(KeyEvent.VK_LEFT);
        boolean right = window.held(KeyEvent.VK_RIGHT);

        if (left) x -= movementMulti;
        if (right) x += movementMulti;

        long constantTime = System.currentTimeMillis();

        velocityY += accelerationY;
        float conditionY = window.getHeight() - height - 1;


        if(jump2){
            if (velocityY < 0)  velocityY *= 0.3;
        }
        if (jump) {
            jumpCounter = System.currentTimeMillis();
        }
        if(constantTime < jumpCounter + 50 && isGrounded){
            isGrounded = false;
            velocityY = -20.0;

        }
        if(isGrounded){
            velocityY = 0.0;
        }
        if (isGrounded == false) y+= velocityY;


        Platform plat = collisionDetect();
        if(plat != null) {
            if(lastY > plat.y+plat.height && y <= plat.y+plat.height) bottom(plat);
            if(lastY+height < plat.y && y+height >= plat.y) top(plat);
            if(lastX > plat.x+plat.width && x <= plat.x+plat.width) right(plat);
            if(lastX +width < plat.x && x + width >= plat.x) left(plat);
        }
        else{
            isGrounded = false;
        }


        if (y >= conditionY){
            y = conditionY;
            isGrounded = true;
        }
    }

    public void top(Platform plat){
        System.out.println("top");
        y = plat.y - height - 1;
        isGrounded = true;
    }
    public void bottom(Platform plat){
        System.out.println("bottom");
        y = plat.y + plat.height;
        velocityY = 0.0;
    }
    public void left(Platform plat){
        System.out.println("left");
        x = plat.x - width-1;
    }
    public void right(Platform plat){
        System.out.println("right");
        x = plat.x + plat.width+1;
    }


    private Platform collisionDetect(){
        LinkedList<Platform> platList = Platform.platformList;
        //x y (top) Dectect
        for (int i = 0; i < platList.size(); i++){
            if (x + width >= platList.get(i).x && x <= platList.get(i).x + platList.get(i).width) {
                if (y + height >= platList.get(i).y && y <= platList.get(i).y + platList.get(i).height) {
                    return platList.get(i);
                }
            }
        }

        return null;
    }

    public float[] lineRect(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh){
        float[] left = lineLine(x1, y1, x2, y2, rx, ry, rx, ry+rh);
        float[] right = lineLine(x1, y1, x2, y2, rx+rw, ry, rx+rw, ry+rh);
        float[] top = lineLine(x1, y1, x2, y2, rx, ry, rx+rw, ry);
        float[] bottom = lineLine(x1, y1, x2, y2, rx, ry+rh, rx+rw, ry+rh);

        if(left != null) {
            left[0] = 0;
            return left;
        }
        if(right != null) {
            right[0] = 1;
            return right;
        }
        if(top != null) {
            top[0] = 2;
            return top;
        }
        if(bottom != null) {
            bottom[0] = 3;
            return bottom;
        }

        return null;
    }

    public float[] lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        float dup = ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / dup;
        float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / dup;

        if(uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1){
            float intersectionX = x1 + (uA * (x2-x1));
            float intersectionY = y1 + (uA * (y2-y1));

            return new float[]{0, intersectionX, intersectionY};
        }
        return null;
    }
}
