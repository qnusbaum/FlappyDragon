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
    private Bitmap background5;
    private int xTranslation;
    private int xTranslationTree;
    private int xTranslationGround;


    public Background(Context context, GameView view) {
        height = context.getResources().getDisplayMetrics().heightPixels;
        width = context.getResources().getDisplayMetrics().widthPixels;
        background1 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer1, width, height);
        background4 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer4, width, height);
        background5 = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.layer5, width, height);
        this.view = view;
        xTranslation=0;
    }

    /**
     * Used to draw background of the application
     * @param  canvas The canvas
     */
    public void draw(Canvas canvas) {
        //TODO : Correct bug
        canvas.drawBitmap(background1,
                new Rect(0, 0, background1.getWidth(), background1.getHeight()),
                new Rect(+xTranslation, 0, width * background1.getWidth() /  background1.getHeight()+xTranslation, height),
                null);
        xTranslation -=1;
        canvas.drawBitmap(background1,
                new Rect(0, 0, background1.getWidth(), background1.getHeight()),
                new Rect(background1.getWidth()+xTranslation, 0, (width * background1.getWidth() /  background1.getHeight())*2+xTranslation, height),
                null);
         if( xTranslation <= -background1.getWidth()){
             xTranslation=0;
         }

         //Gestion des arbres
        canvas.drawBitmap(background4,
                new Rect(0, 0, background1.getWidth(), background1.getHeight()),
                new Rect(+xTranslationTree, 0, width * background4.getWidth() /  background4.getHeight()+xTranslationTree, height),
                null);
        xTranslationTree -=10;
        canvas.drawBitmap(background4,
                new Rect(0, 0, background1.getWidth(), background1.getHeight()),
                new Rect(background4.getWidth()+xTranslationTree, 0, (width * background4.getWidth() /  background4.getHeight())*2+xTranslationTree, height),
                null);
        if( xTranslationTree <= -background1.getWidth()){
            xTranslationTree=0;
        }

        //Gestion du sol
        canvas.drawBitmap(background5,
                new Rect(0, 0, background5.getWidth(), background5.getHeight()),
                new Rect(+xTranslationGround, 0, (width * background5.getWidth() /  background5.getHeight())+xTranslationGround, height),
                null);
        xTranslationGround -=10;
        canvas.drawBitmap(background5,
                new Rect(0, 0, background5.getWidth(), background5.getHeight()),
                new Rect(background5.getWidth()+xTranslationGround, 0, (width * background5.getWidth() /  background5.getHeight())*2+xTranslationGround, height),
                null);
        if( xTranslationGround <= -background5.getWidth()){
            xTranslationGround=0;
        }
    }
}