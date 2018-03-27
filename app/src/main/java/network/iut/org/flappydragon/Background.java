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
    private Bitmap background4;
    private int xTranslation;

    public Background(Context context, GameView view) {
        height = context.getResources().getDisplayMetrics().heightPixels;
        width = context.getResources().getDisplayMetrics().widthPixels;
        background1 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer1, width, height);
        background4 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer4, width, height);
        this.view = view;
        xTranslation=0;
    }

    public void draw(Canvas canvas) {
         canvas.drawBitmap(background1,
                 new Rect(xTranslation, 0, background1.getWidth(), background1.getHeight()),
                 new Rect(0, 0, background1.getWidth(), height),
                 null);
         xTranslation +=10;
        canvas.drawBitmap(background1,
                new Rect(background1.getWidth()+xTranslation, 0, background1.getWidth(), background1.getHeight()),
                new Rect(0, 0, background1.getWidth(), height),
                null);
         if( xTranslation >= background1.getWidth()){
             xTranslation=0;
         }
         canvas.drawBitmap(background4, new Rect(0, 0, background1.getWidth(), background1.getHeight()), new Rect(0, 0, width, height), null);
    }
}