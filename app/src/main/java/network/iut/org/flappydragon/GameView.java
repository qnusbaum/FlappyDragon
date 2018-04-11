package network.iut.org.flappydragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

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
        boolean collision;
        for(Ennemy ennemy : ennemies){
            ennemy.move();
            if(isCollisionDetected(player,player.getX(),player.getY(), ennemy,ennemy.getX(),ennemy.getY())){
                Log.e("Collision","We touch a butterfly ! ");
                stopTimer();
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

    /**
     * Codé récupéré sur https://medium.com/@euryperez/android-pearls-pixel-perfect-collision-detection-with-no-framework-53a5137baca2
     * Check pixel-perfectly if two views are colliding
     *
     * @param player player
     * @param x1 first view position in x-axis
     * @param y1 first view position in y-axis
     * @param ennemy ennemy
     * @param x2 second view position in x-axis
     * @param y2 second view position in y-axis
     * @return boolean
     */
    public static boolean isCollisionDetected(Player player, int x1, int y1,
                                              Ennemy ennemy, int x2, int y2) {

        Bitmap bitmap1 = player.getBitmap();
        Bitmap bitmap2 = ennemy.getBitmap();

        if (bitmap1 == null || bitmap2 == null) {
            throw new IllegalArgumentException("bitmaps cannot be null");
        }

        Rect bounds1 = new Rect(x1, y1, x1 + bitmap1.getWidth(), y1 + bitmap1.getHeight());
        Rect bounds2 = new Rect(x2, y2, x2 + bitmap2.getWidth(), y2 + bitmap2.getHeight());

        if (Rect.intersects(bounds1, bounds2)) {
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = bitmap1.getPixel(i - x1, j - y1);
                    int bitmap2Pixel = bitmap2.getPixel(i - x2, j - y2);
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        bitmap1 = null;
                        bitmap2 = null;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Codé récupéré sur https://medium.com/@euryperez/android-pearls-pixel-perfect-collision-detection-with-no-framework-53a5137baca2
     * Get a Bitmap from a specified View
     *
     * @param v View
     * @return Bitmap
     */
    private static Bitmap getViewBitmap(View v) {
        if (v.getMeasuredHeight() <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v.measure(specWidth, specWidth);
            Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    /**
     * Codé récupéré sur https://medium.com/@euryperez/android-pearls-pixel-perfect-collision-detection-with-no-framework-53a5137baca2
     * Check if pixel is not transparent
     *
     * @param pixel int
     * @return boolean
     */
    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }

    /**
     * Codé récupéré sur https://medium.com/@euryperez/android-pearls-pixel-perfect-collision-detection-with-no-framework-53a5137baca2
     * Get the collision bounds from two rects
     *
     * @param rect1 Rect
     * @param rect2 Rect
     * @return Rect
     */
    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = Math.max(rect1.left, rect2.left);
        int top = Math.max(rect1.top, rect2.top);
        int right = Math.min(rect1.right, rect2.right);
        int bottom = Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

}
