package game.asteroidgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by Felix on 11/04/16.
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new GamePanel(this));
        //set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onStop()  {
        super.onStop();
    }

}
