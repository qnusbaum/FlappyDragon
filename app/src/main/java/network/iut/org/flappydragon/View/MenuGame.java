package network.iut.org.flappydragon.View;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import network.iut.org.flappydragon.R;

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
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButtonChecked = (RadioButton) radioGroup.findViewById(radioButtonID);
                String selectedtext = (String) radioButtonChecked.getText();
                Log.e(selectedtext,selectedtext);
                initMenu();
            }
        });

    }

    public void initMenu() {
        setContentView(R.layout.menu_game);
        Button jouer = (Button) findViewById(R.id.btnJouer);
        Button option = (Button) findViewById(R.id.btnOptions);

        /*SharedPreferences sharedPref = .getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_high_score_key), newHighScore);
        editor.commit();*/

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
