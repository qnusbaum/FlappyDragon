package network.iut.org.flappydragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Classe Ennemy
 *
 * Cette classe permet de gérer les différents ennemy présents en jeu
 */
public class Ennemy {
    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;
    private Bitmap bitmap;
    private final Bitmap downBitmap;
    private final Bitmap upBitmap;
    private final byte frameTime;
    private int frameTimeCounter;
    private final int width;
    private final int height;
    private int x;
    private int y;
    private float speedX;
    private float speedY;
    private GameView view;

    public Ennemy(Context context, GameView view) {
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int width = context.getResources().getDisplayMetrics().widthPixels;
        if(globalBitmap == null) {
            Log.e("TEST", "Height : " + height + ", width : " + width);
            globalBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.ennemy2, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
        this.frameTime = 3;		// the frame will change every 3 runs
        this.y = context.getResources().getDisplayMetrics().heightPixels / 2;	// Startposition in the middle of the screen

        this.view = view;
        int nombreAleatoire = 0 + (int)(Math.random() * ((context.getResources().getDisplayMetrics().heightPixels - 0) + 1));
        this.x = nombreAleatoire;

        this.speedX = 5;
        downBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.ennemy2, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());
        upBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.ennemy3, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());

    }

    public void move() {
        changeToNextFrame();
        this.x += speedX;
        this.y += speedY;
    }

    protected void changeToNextFrame() {
        this.frameTimeCounter++;
        if (this.frameTimeCounter >= this.frameTime) {
            //TODO Change frame
            this.frameTimeCounter = 0;
        }
    }

    private float getSpeedTimeDecrease() {
        return view.getHeight() / 320;
    }

    private float getMaxSpeed() {
        return view.getHeight() / 51.2f;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
}
