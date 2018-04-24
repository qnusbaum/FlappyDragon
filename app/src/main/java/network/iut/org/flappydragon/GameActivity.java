package network.iut.org.flappydragon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import network.iut.org.flappydragon.View.GameView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }
}
