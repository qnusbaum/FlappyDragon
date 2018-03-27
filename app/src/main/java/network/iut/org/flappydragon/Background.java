package network.iut.org.flappydragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Background {
    private int height;
    private int width;
    private GameView view;
    private Bitmap background1;
    private Bitmap background2;
    private Bitmap background3;
    private Bitmap background4;
    private Bitmap background5;
    private Bitmap background6;
    private Bitmap background7;
    private Bitmap background8;
    private Bitmap background9;
    private Bitmap background10;

    public Background(Context context, GameView view) {
        height = context.getResources().getDisplayMetrics().heightPixels;
        width = context.getResources().getDisplayMetrics().widthPixels;
        background1 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer1, width, height);
        background4 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer4, width, height);
        this.view = view;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background1, new Rect(0, 0, background1.getWidth()/2, background1.getHeight()), new Rect(0, 0, width, height), null);
        canvas.drawBitmap(background4, new Rect(0, 0, background1.getWidth(), background1.getHeight()), new Rect(0, 0, width, height), null);
    }
}