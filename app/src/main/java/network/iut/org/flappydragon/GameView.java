package network.iut.org.flappydragon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class GameView extends SurfaceView implements Runnable {
    public static final long UPDATE_INTERVAL = 10; // = 20 FPS
    private SurfaceHolder holder;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Player player;
    private Background background;
    private boolean start = true;
    private boolean gameOver = false;
    private Context context;

    public GameView(Context context) {
        super(context);
        this.context = context;
        player = new Player(context, this);
//        player.setToMiddle();
        background = new Background(context, this);
        holder = getHolder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GameView.this.run();
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(start) {
                start();
            } else {
                if (gameOver) {
                    start = true;
                    restartGame();
                } else {
                    Log.i("PLAYER", "PLAYER TAPPED");
                    this.player.onTap();
                }
            }
        }
        return true;
    }

    public void onLoose() {
        System.out.println("perdu");
        gameOver = true;
        stopTimer();
    }

    private void start() {
        start = false;
        startTimer();
    }

    private void restartGame() {
        this.player = new Player(context, this);
//        player.setToMiddle();
        gameOver = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                GameView.this.run();
            }
        });
    }

    private void startTimer() {
        Log.i("TIMER", "START TIMER");
        setUpTimerTask();
        timer = new Timer();
        timer.schedule(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    private void setUpTimerTask() {
        stopTimer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                GameView.this.run();
            }
        };
    }

    @Override
    public void run() {
        player.move();
        draw();
    }

    private void draw() {
        while(!holder.getSurface().isValid()){
			/*wait*/
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            drawCanvas(canvas);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    private void drawCanvas(Canvas canvas) {
        background.draw(canvas);
        player.draw(canvas);
        if (start) {
            canvas.drawText("START", canvas.getWidth() / 2, canvas.getHeight() / 2, new Paint());
        } else if (gameOver){
            canvas.drawText("GAME OVER, TOUCH TO RESTART", canvas.getWidth() / 3, canvas.getHeight() / 2, new Paint());
        }
    }
}
