package game.asteroidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Felix on 12/04/16.
 */
public class Player extends GameObject {

    private int score;
    private boolean up;
    private boolean down;
    private boolean playing;


    private Bitmap image;


    public Player(Bitmap res) {
        x = 290;
        y = GamePanel.HEIGHT/2;
        image = res;
        height = 80;
        width = 80;
        playing = true;
    }

    public void update() {
        if(up) {
            y += -15;
        }
        if(down) {
            y+= 15;
        }


    }

    public void draw(Canvas c) {

        //player is double the size, so half the coordinates
        c.drawBitmap(image, x/2, y/2, null);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean d) {
        this.up = d;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean d) {
        this.down = d;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
