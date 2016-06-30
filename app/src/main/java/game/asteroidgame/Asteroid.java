package game.asteroidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Felix on 12/04/16.
 */
public class Asteroid extends GameObject {

    private Bitmap image;
    Random r = new Random();


    public Asteroid(Bitmap res) {
        x = GamePanel.WIDTH + 50;
        y = r.nextInt(GamePanel.HEIGHT - 25) + 1;
        dx = r.nextInt(11) + 20;

        image = res;
        height = 60;
        width = 60;
    }

    public void update() {
        x -= dx;
    }
    public void draw(Canvas c) {
        c.drawBitmap(image, x, y, null);

        if(GamePanel.debug) {
            Paint p = new Paint();
            p.setColor(Color.YELLOW);

            c.drawLine(x, y, x - 20, y - 20, p);
            c.drawLine(x + width, y + height, x + width + 20, y + height + 20, p);

        }
    }


}
