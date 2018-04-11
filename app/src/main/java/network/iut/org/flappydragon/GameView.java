package network.iut.org.flappydragon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView implements Runnable {
    public static final long UPDATE_INTERVAL = 10; // = 20 FPS
    private SurfaceHolder holder;
    private boolean paused = true;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Player player;
    private List<Ennemy> ennemies;
    private Background background;
    private Context context;
    private int vitesseSpawn;

    public GameView(final Context context) {
        super(context);
        vitesseSpawn = 500;
        this.context = context;
        player = new Player(context, this);
        ennemies = new ArrayList<>();
        background = new Background(context, this);
        holder = getHolder();
        //On créer un timer afin de créer des ennemis à intervalle de temps régulier
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("Enemy","Create a new enemy");
                ennemies.add(new Ennemy(context));
            }
        }, 0, vitesseSpawn);
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
            if(paused) {
                resume();
            } else {
                Log.i("PLAYER", "PLAYER TAPPED");
                this.player.onTap();
            }
        }
        return true;
    }

    private void resume() {
        paused = false;
        startTimer();
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
        //TODO: Check collisions
        Log.e("RUN","On run l'application");
        for(Ennemy ennemy : ennemies){
            ennemy.move();
            if(player.getPosition().intersect(ennemy.getPosition())){
                Log.e("Collision","We touch a butterfly ! ");
                stopTimer();
                //Use 'loose' method
            }
        }
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
        List<Ennemy> toRemove = new ArrayList<>();
        for(Ennemy ennemy : ennemies){
            if(ennemy.getX() <= 0){
                toRemove.add(ennemy);
            }else{
                ennemy.draw(canvas);
            }
        }
        ennemies.removeAll(toRemove);
        if (paused) {
            canvas.drawText("PAUSED", canvas.getWidth() / 2, canvas.getHeight() / 2, new Paint());
        }
    }

}
