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
    private int xTranslationArbre;

    public Background(Context context, GameView view) {
        height = context.getResources().getDisplayMetrics().heightPixels;
        width = context.getResources().getDisplayMetrics().widthPixels;
        background1 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer1, width, height);
        background4 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer4, width, height);
        this.view = view;
        xTranslation=0;
    }

    public void draw(Canvas canvas) {
        //Gestion du ciel
         canvas.drawBitmap(background1,
                 new Rect(0, 0, background1.getWidth(), background1.getHeight()),
                 new Rect(+xTranslation, 0, background1.getWidth()+xTranslation, height),
                 null);
         xTranslation -=1;
         canvas.drawBitmap(background1,
                new Rect(0, 0, background1.getWidth(), background1.getHeight()),
                new Rect(background1.getWidth()+xTranslation, 0, background1.getWidth()*2+xTranslation, height),
                null);
         if( xTranslation <= -background1.getWidth()){
             xTranslation=0;
         }

         //Gestion des arbres
        canvas.drawBitmap(background4,
                new Rect(0, 0, background1.getWidth(), background1.getHeight()),
                new Rect(+xTranslationArbre, 0, width * background1.getWidth() /  background1.getHeight()+xTranslationArbre, height),
                null);
        xTranslationArbre -=10;
        canvas.drawBitmap(background4,
                new Rect(0, 0, background1.getWidth(), background1.getHeight()),
                new Rect(background1.getWidth()+xTranslationArbre, 0, (width * background1.getWidth() /  background1.getHeight())*2+xTranslationArbre, height),
                null);
        if( xTranslationArbre <= -background1.getWidth()){
            xTranslationArbre=0;
        }
    }
}