package game.asteroidgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Felix on 12/04/16.
 */
public class Missile extends GameObject {

    Player p;
    Paint paint = new Paint();

    public Missile(Player p) {
        this.p = p;
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);
        dx = 40;
        x = p.getX() + 10;
        y = p.getY() + 38;
        width = 30;
        height = 6;
    }

    public void update() {
        x += dx;
    }

    public void draw(Canvas c) {
        c.drawLine(x, y, x + 30, y, paint);

        if(GamePanel.debug){
            Paint p = new Paint();
            p.setColor(Color.YELLOW);

            c.drawLine(x, y, x - 20, y - 20, p);
            c.drawLine(x + width, y + height, x + width + 20, y + height + 20, p);
        }
    }
}
