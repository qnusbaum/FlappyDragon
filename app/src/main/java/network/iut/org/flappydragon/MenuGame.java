package network.iut.org.flappydragon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Android on 11/04/2018.
 */

public class MenuGame extends Activity {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.menu_game);
        super.onCreate(savedInstanceState);
        Button jouer = (Button) findViewById(R.id.btnJouer);
        Button option = (Button) findViewById(R.id.btnOptions);

        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(new GameView(getApplicationContext()));
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.options);
            }
        });
    }
}
