package game.asteroidgame;

/**
 * Created by Felix on 11/04/16.
 */
public class UpdateThread extends Thread {

    private int UPS = 30;
    private GamePanel gamePanel;
    private double averageUPS;
    public boolean running;

    public UpdateThread() {
        super();
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/UPS;

        while(running) {
            startTime = System.nanoTime();

            //Try locking the canvas for pixel editing

            try {
                this.gamePanel.update();
            } catch (Exception e) { }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                this.sleep(waitTime);
            } catch (Exception e) {}



            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == UPS) {
                averageUPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("AvgUPS: " + averageUPS);
            }
        }
    }

    public void setRunning(boolean r) {
        running = r;
    }


}
