package game.asteroidgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Felix on 11/04/16.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int MOVESPEED = -5;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final boolean debug = false;

    private long astartTime;
    private long mstartTime;
    private long startReset;
    private boolean reset = true;
    private boolean disappear = false;
    private boolean explosionCreated = false;
    private boolean newGameCreated = false;



    private UpdateThread updateThread;
    private DrawThread drawThread;
    private Background background;
    private Player player;
    private Explosion explosion;

    private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    private ArrayList<Missile> missiles = new ArrayList<Missile>();

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        /**
         * load resources, start threads
         */
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.spacebg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.spaceship1));

        updateThread = new UpdateThread();
        drawThread = new DrawThread(getHolder(), this);

        updateThread.setRunning(true);
        drawThread.setRunning(true);
        updateThread.start();
        drawThread.start();


        astartTime = System.nanoTime();
        mstartTime = System.nanoTime();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if(player.isPlaying()) {
            MotionEvent.PointerCoords p1 = new MotionEvent.PointerCoords();
            MotionEvent.PointerCoords p2 = new MotionEvent.PointerCoords();
            e.getPointerCoords(0, p1);

            if(e.getPointerCount() > 1) {
                e.getPointerCoords(1, p2);

                if ((p1.x < WIDTH / 2) && (p1.y < HEIGHT / 2)) {
                    player.setUp(true);
                    player.setDown(false);
                }
                if ((p1.x < WIDTH / 2) && (p1.y > HEIGHT / 2)) {
                    player.setDown(true);
                    player.setUp(false);
                }


                if (p2.x > WIDTH / 2) { addMissile(); }
            } else if(e.getPointerCount() == 1) {

                if ((p1.x < WIDTH / 2) && (p1.y < HEIGHT / 2)) {
                    player.setUp(true);
                    player.setDown(false);
                }
                if ((p1.x < WIDTH / 2) && (p1.y > HEIGHT / 2)) {
                    player.setDown(true);
                    player.setUp(false);
                }


                if (p1.x > WIDTH / 2) {
                    addMissile();
                }
            }



            if(e.getAction() == MotionEvent.ACTION_UP) {
                player.setDown(false);
                player.setUp(false);
                System.out.println("aufi");
            }

            reset = false;
        }

        return true;

        /*
        if(!reset) {
                newGame();
                startReset = System.nanoTime();
            }
            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed > 1000 && !newGameCreated)
            {
                newGame();
            }

        }
         */

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)

        {
            counter++;
            try{updateThread.setRunning(false);
                updateThread.join();
                retry = false;
                updateThread = null;

            }catch(InterruptedException e){e.printStackTrace();}

        }
        retry = true;
        System.out.println("UpdateThreadCounter: " + counter);
        counter = 0;

        while(retry && counter<1000)
        {
            counter++;
            try{drawThread.setRunning(false);
                drawThread.join();
                retry = false;
                drawThread = null;

            }catch(InterruptedException e){e.printStackTrace();}

        }
    }

    public void draw(Canvas canvas) {
        int defaultScaling = canvas.save();
        float scalex = getWidth()/(WIDTH * 1.0f);
        float scaley = getHeight()/(HEIGHT * 1.0f);
        canvas.scale(scalex, scaley);

        background.draw(canvas);
        for(Asteroid a : asteroids) {
            a.draw(canvas);
        }

        for(Missile m : missiles) {
            m.draw(canvas);
        }
        //double the size for the player
        canvas.restoreToCount(defaultScaling);
        canvas.scale(scalex * 2, scaley * 2);
        if(!disappear) player.draw(canvas);
        explosion.draw(canvas);



        canvas.restoreToCount(defaultScaling);
    }

    public void update() {

        if(player.isPlaying()) {
            background.update();
            player.update();
            if(explosion != null) {
                explosion.update();
            }
            //spawn the asteroids

            long missileElapsed = (System.nanoTime()-astartTime)/1000000;
            if(missileElapsed >(300 - player.getScore()/4)){
                astartTime = System.nanoTime();
                asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1)));
            }


            for (int i = 0; i < asteroids.size(); i++) {
                if(asteroids.get(i).getX() < -50) {
                    asteroids.remove(i);
                }
            }

            for(Asteroid a : asteroids) {
                a.update();
            }

            for(Missile m : missiles) {
                m.update();
            }

            for(int i = 0; i < asteroids.size(); i++) {
                for(Missile m : missiles) {
                    if(Rect.intersects(asteroids.get(i).getRectangle(), m.getRectangle())) {
                        asteroids.remove(i);
                        missiles.remove(m);
                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),m.getX() - 50, m.getY() - 50, 100, 100, 25);
                    }
                }
                if(Rect.intersects(asteroids.get(i).getRectangle(), player.getRectangle())) {
                    /**
                     * Player hit asteroid -> newgame
                     */
                    asteroids.remove(i);

                    player.setPlaying(false);
                    disappear = true;
                }
            }
        } else {
            if(!explosionCreated) {
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX() + 15, player.getY() - 55, 100, 100, 25);
                explosion.setDelay(100);
                explosionCreated = true;
                startReset = System.nanoTime();
            }
            if(!(explosion.playedOnce())) explosion.update();
            if(((System.nanoTime() - startReset)/1000000) > 1600) {
                newGame();
            }

        }

    }

    public void addMissile() {
        long missileElapsed = (System.nanoTime()-mstartTime)/1000000;
        if(missileElapsed > 500){
            mstartTime = System.nanoTime();
            missiles.add(new Missile(player));
        }
    }

    @Deprecated
    public void resetGame() {
        System.out.println("ballaballa");
        player.setScore(0);
        player.setY(HEIGHT / 2);
        reset = true;
    }


    public void newGame() {
        explosionCreated = false;
        explosion = null;
        System.out.println("rendlrendlrendl");

        player.setScore(0);
        player.setY(HEIGHT / 2);
        player.setPlaying(true);
        disappear = false;
        asteroids.clear();
        missiles.clear();


        newGameCreated = true;

    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

}
