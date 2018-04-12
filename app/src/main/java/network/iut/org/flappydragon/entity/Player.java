package network.iut.org.flappydragon.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import network.iut.org.flappydragon.View.GameView;
import network.iut.org.flappydragon.R;
import network.iut.org.flappydragon.Util;

public class Player {
    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;
    private final int height;
    private final int width;
    private GameView view;
    private Bitmap bitmap;
    private final Bitmap downBitmap;
    private final Bitmap middleBitmap;
    private final Bitmap upBitmap;
    private final byte frameTime;
    private int frameTimeCounter;
    private final int imgWidth;
    private final int imgHeight;
    private int x;
    private int y;
    private float speedX;
    private float speedY;
    private Context context;

    public Player(Context context, GameView view) {
        this.view = view;
        height = context.getResources().getDisplayMetrics().heightPixels;
        width = context.getResources().getDisplayMetrics().widthPixels;

        if(globalBitmap == null) {
            Log.e("TEST", "Height : " + height + ", imgWidth : " + width);
            globalBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.frame1, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());
        }
        this.context = context;
        this.bitmap = globalBitmap;
        this.imgWidth = this.bitmap.getWidth();
        this.imgHeight = this.bitmap.getHeight();
        this.frameTime = 3;		// the frame will change every 3 runs
        this.y = height / 2;	// Startposition in the middle of the screen

        this.x = this.imgWidth / 6;
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
        return - height / 100;
    }

    private float getTabSpeed() {
        return -height / 16f;
    }

    public void move() {
        changeToNextFrame();

        if (speedY < 0) {
            if (this.y <= 0) {
                Log.i("Position", "En haut de l'écran");
                this.speedY=0;
                this.y=1;
                view.onLoose();
            }else{
                // The character is moving up
                Log.i("Move", "Moving up");
                speedY = speedY * 2 / 3 + getSpeedTimeDecrease() / 2;
            }

        } else {
            Log.i("Move", "Moving down with up wings");
            this.bitmap = downBitmap;
            if(this.y >= height- imgHeight){
                Log.i("Position", "En bas de l'écran");
                this.y = height - imgHeight;
                this.speedY = 0;
                view.onLoose();
            }else{
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
        return height / 320;
    }

    private float getMaxSpeed() {
        return height / 51.2f;
    }

    public Rect getPosition(){
        return new Rect(x,y,x+bitmap.getHeight(),y+bitmap.getWidth());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void setToMiddle() {
        this.y = context.getResources().getDisplayMetrics().heightPixels / 3;
    }

    public int  getX(){
        return this.x;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getY(){
        return this.y;
    }
}