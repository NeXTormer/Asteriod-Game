package game.asteroidgame;

import android.graphics.Bitmap;

/**
 * Created by Felix on 13/04/16.
 */
public class Animation {

    protected Bitmap[] frames;
    protected int currentFrame;
    protected long startTime;
    protected boolean playedOnce;
    protected long delay;

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();

    }

    public void setDelay(long d) {
        delay = d;
    }

    public void setFrame(int i) {
        currentFrame = i;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();

        }
        if(currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;

        }
    }

    public Bitmap getImage() {
        return frames[currentFrame];
    }

    public int getFrame() {
        return currentFrame;
    }

    public boolean playedOnce() {
        return playedOnce;
    }

}
