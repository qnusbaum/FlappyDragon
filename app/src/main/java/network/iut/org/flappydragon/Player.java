package network.iut.org.flappydragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;

public class Player {
    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;
    private Bitmap bitmap;
    private final Bitmap downBitmap;
    private final Bitmap middleBitmap;
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

    public Player(Context context, GameView view) {
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int width = context.getResources().getDisplayMetrics().widthPixels;

        if(globalBitmap == null) {
            Log.e("TEST", "Height : " + height + ", width : " + width);
            globalBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.frame1, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
        this.frameTime = 3;		// the frame will change every 3 runs
        this.y = context.getResources().getDisplayMetrics().heightPixels / 2;	// Startposition in the middle of the screen

        this.view = view;
        this.x = this.width / 6;
        this.speedX = 0;
        downBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.frame1, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());
        middleBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.frame2, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());
        upBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.frame3, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());
    }

    public void onTap() {
        this.speedY = getTabSpeed();
        this.y += getPosTabIncrease();
        Log.i("wings down", "Moving up");
        this.bitmap = upBitmap;
    }

    private float getPosTabIncrease() {
        return - view.getHeight() / 100;
    }

    private float getTabSpeed() {
        return -view.getHeight() / 16f;
    }

    public void move() {
        changeToNextFrame();

        if (speedY < 0) {
            if (this.y <= 0) {
                Log.i("Position", "En haut de l'écran");
                this.speedY = 0;
                this.y = 1;
            } else {
                // The character is moving up
                Log.i("Move", "Moving up");
                speedY = speedY * 2 / 3 + getSpeedTimeDecrease() / 2;
            }

        } else {
            Log.i("Move", "Moving down with up wings");
            this.bitmap = downBitmap;
            if(this.y >= view.getHeight()-height){
                Log.i("Position", "En bas de l'écran");
                this.y = view.getHeight() - height;
                this.speedY = 0;
            } else {
                this.speedY += getSpeedTimeDecrease();
            }
        }
        if (this.speedY > getMaxSpeed()) {
            // speed limit
            this.speedY = getMaxSpeed();
        }

        // manage frames
/*        if(row != 3){
            // not dead
            if(speedY > getTabSpeed() / 3 && speedY < getMaxSpeed() * 1/3){
                row = 0;
            }else if(speedY > 0){
                row = 1;
            }else{
                row = 2;
            }
        }
*/
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