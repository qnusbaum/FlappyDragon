package network.iut.org.flappydragon.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import network.iut.org.flappydragon.Background;
import network.iut.org.flappydragon.Util;
import network.iut.org.flappydragon.R;
import network.iut.org.flappydragon.entity.Enemy;
import network.iut.org.flappydragon.entity.Player;

public class GameView extends SurfaceView implements Runnable {
    public static final long UPDATE_INTERVAL = 10; // = 20 FPS
    private int defaultValue = 10;
    private SurfaceHolder holder;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Player player;
    private List<Enemy> enemies;
    private Background background;
    private boolean start = true;
    private boolean gameOver = false;
    private Context context;
    private int vitesseSpawn;
    Bitmap home;

    public GameView(final Context context) {
        super(context);
        this.context = context.getApplicationContext();
        //try to initialize a home button to go back to menu page
        home = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.home, 100, 100);
        setVitesseSpawn(MenuGame.choixDifficulte);
        player = new Player(context, this);
        enemies = new ArrayList<>();
        background = new Background(context, this);
        holder = getHolder();
        //On créer un timer afin de créer des ennemis à intervalle de temps régulier
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!start) {
                    Log.e("Enemy", "Create a new enemy");
                    enemies.add(new Enemy(context,defaultValue));
                }
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
        gameOver = false;
        enemies.clear();
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
        Log.e("RUN","On run l'application");
        for(Enemy enemy : enemies){
            enemy.move();
            if(isCollisionDetected(player,player.getX(),player.getY(), enemy,enemy.getX(),enemy.getY())){
                Log.e("Collision","We touch a butterfly ! ");
                onLoose();
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        background.draw(canvas);
        player.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(30);
        //try to display the home button to go back to menu page
        canvas.drawBitmap(home, canvas.getWidth() * 14 , canvas.getHeight() * 24, paint);
        canvas.drawText("Meilleur score : " + prefs.getInt("hightscore", 0), canvas.getWidth() / 14, canvas.getHeight() / 24, paint);
        canvas.drawText("Score : "+enemies.size(), canvas.getWidth() / 14, canvas.getHeight() / 17, paint);
        List<Enemy> toRemove = new ArrayList<>();
        for(Enemy ennemy : new ArrayList<>(enemies)){
            if(ennemy.getX() <= 0){
                toRemove.add(ennemy);
            }else{
                ennemy.draw(canvas);
            }
        }
        enemies.removeAll(toRemove);
        if (start) {
            canvas.drawText("START", canvas.getWidth() / 2, canvas.getHeight() / 2, new Paint());
        } else if (gameOver){
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    //TODO : We tryed this, it worked on a branch but after merging or on other branch, it doesn't work anymore
                    //TODO : I let it commented in order you can maybe explain us why it cause a crash of the app
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage(R.string.textRestart);
//                    builder.setCancelable(false);
//                    builder.setPositiveButton(R.string.restart, new OkOnClickListener());
//                    builder.setNegativeButton(R.string.goMenu, new CancelOnClickListener());
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
                }
            });
        }
        if (enemies.size() > prefs.getInt("hightscore", 0)) {
            editor.putInt("hightscore", enemies.size());
            editor.commit();
        }
    }

    private final class CancelOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            System.exit(1);
        }
    }

    private final class OkOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(context, "Taper deux fois l'écran pour recommencer !",
                    Toast.LENGTH_LONG).show();
            new GameActivity();
        }
    }

    private void setVitesseSpawn(int difficulte) {
        switch (difficulte) {
            case 1 :
                this.vitesseSpawn = 1000;
                break;
            case 2 :
                this.vitesseSpawn = 500;
                break;
            case 3 :
                this.vitesseSpawn = 150;
                this.defaultValue = 20;
                break;
        }
    }

    /**
     * Code récupéré sur https://medium.com/@euryperez/android-pearls-pixel-perfect-collision-detection-with-no-framework-53a5137baca2
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
                                              Enemy ennemy, int x2, int y2) {

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
