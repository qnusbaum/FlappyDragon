package network.iut.org.flappydragon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }

    public void RestartGame(View view) {
        View.OnClickListener restartGame = new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(new GameView(getApplicationContext()));
            }
        };
    }
}
