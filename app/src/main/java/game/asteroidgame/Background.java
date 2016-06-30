package game.asteroidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Felix on 12/04/16.
 */
public class Background {

    private Bitmap image;
    private int x, dx, y;

    public Background(Bitmap res) {
        dx = GamePanel.MOVESPEED;
        image = res;
    }

    public void update() {
        x += dx;
        if(x < -GamePanel.WIDTH) {
            x = 0;
        }
    }

    public void draw(Canvas c) {
        c.drawBitmap(image, x, y, null);
        if(x  < 0) {
            c.drawBitmap(image, x + GamePanel.WIDTH, y, null);
        }
    }
}
