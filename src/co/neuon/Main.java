package co.neuon;

import co.neuon.Network.Client;
import co.neuon.Network.Connectable;
import co.neuon.Network.Server;
import co.neuon.Objects.Text;

public class Main implements Runnable {

    public static void main(String[] args){
        new Main();
    }

    Window window;
    public Game game;
    public Connectable connectable;

    Text fpsCounter;
    public Text input;

    public Executable connect;

    double currentTimeFrameLimiter, deltaTimeFrameLimiter, lastTimeFrameLimiter, elapsedFrameLimiter = 0;
    double currentTimeGame, deltaTimeGame, lastTimeGame;
    int fps = 60;
    boolean shouldRun = true;

    public Main(){
        new Thread(this).start();
    }

    @Override
    public void run() {
        initialise();
        mainLoop();
        close();
    }

    public void initialise(){
        window = new Window(this);
        game = new Game(window);
        game.load(()->{
            //FPS
            fpsCounter = game.createText("FPS: ", 2, 12);

            game.createPlatform(200,20, 100, 300);
            game.createPlayer(20,60,200,100);

//            game.createRect(130, 65, 15, 15);
//            game.createText("Client", 60, 32);
//
//            //TextBox 'IP:'
//            int ipY = 40;
//            game.createRect(120, 14, 20, ipY);
//            game.createText("IP:", 22, ipY+12);
//            input = game.createText("", 38, ipY+12);
//
//
//            int connectY = 60;
//            game.createRect(60, 14, 50, connectY);
//            game.createText("Connect", 56, connectY+12);
//
//            //Player Character
////            game.createCircle(50, 50, 50);
//            //Cursor follower
////            game.createCursor();
        });

        connect = ()->{
            if(input.getText() == ""){
                connectable = new Server(game);

                game.load(()->{
                    game.createText("Server", 60, 32);
                });
            }else{
                connectable = new Client(input.getText());
                game.load(()->{
                    game.createText("Client", 60, 32);
                });
            }
        };

    }

    public void mainLoop() {
        double idealFrameTime = 1000000000/fps;
        lastTimeFrameLimiter = System.nanoTime();
        while (shouldRun) {

            currentTimeFrameLimiter = System.nanoTime();
            deltaTimeFrameLimiter = currentTimeFrameLimiter - lastTimeFrameLimiter;
            lastTimeFrameLimiter = currentTimeFrameLimiter;
            elapsedFrameLimiter +=deltaTimeFrameLimiter;

            while(elapsedFrameLimiter > idealFrameTime){
                elapsedFrameLimiter -=idealFrameTime;

                currentTimeGame = System.nanoTime();
                deltaTimeGame = (currentTimeGame - lastTimeGame)/1000000000;
                lastTimeGame = currentTimeGame;

                if(connectable != null)connectable.tick();
                window.tick();
                game.tick();
                game.render();
                fps();
            }
        }
    }

    public double deltaTime(){
        return deltaTimeGame;
    }

    public void close(){

    }

    int counter = 0;
    double lastTime = System.nanoTime();
    private void fps(){
        counter ++;
        if(System.nanoTime() - lastTime > 1000000000){
            fpsCounter.setText("FPS: "+counter);
            counter = 0;
            lastTime = System.nanoTime();
        }
    }
}


/*
    public void mainLoop() {
        double idealFrameTime = 1.0/fps;
        lastTime = System.nanoTime()/1000000000.0;
        while (shouldRun) {

            currentTime = System.nanoTime()/1000000000.0;
            deltaTime = currentTime - lastTime;
    //            System.out.println(deltaTime);
            if(deltaTime!=0)inverseDeltaTime = 1.0/deltaTime;
            lastTime = currentTime;
            timeSinceLastFrame +=deltaTime;

            game.tick();

            time += deltaTime;
            while(timeSinceLastFrame > idealFrameTime){
                currentTimeGame = System.nanoTime()/1000000000.0;
                deltaTimeGame = currentTimeGame - lastTimeGame;
                lastTimeGame = currentTimeGame;
    //                System.out.println("Tick");
                timeSinceLastFrame-=idealFrameTime;
                game.render();
                fps();
            }
        }
    }

        public double deltaTime(){
            return deltaTime;
        }

            time += deltaTimeFrameLimiter;
    int counter = 0;
    double time = 0;
    private void fps(){
        counter ++;
        if(time > 1){
            fpsCounter.setText("FPS: "+counter);
            counter = 0;
            time -= 1;
        }
    }
*/