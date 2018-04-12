package network.iut.org.flappydragon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by Android on 11/04/2018.
 */

public class MenuGame extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMenu();
    }

    public void initGame() {
        setContentView(new GameView(getApplicationContext()));
    }

    public void initOption() {
        setContentView(R.layout.options);
        Button retour = (Button) findViewById(R.id.btnRetour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMenu();
            }
        });
        //RadioGroup
    }

    public void initMenu() {
        setContentView(R.layout.menu_game);
        Button jouer = (Button) findViewById(R.id.btnJouer);
        Button option = (Button) findViewById(R.id.btnOptions);

        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGame();
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initOption();
            }
        });
    }
}
