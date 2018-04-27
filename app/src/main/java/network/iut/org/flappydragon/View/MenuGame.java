package network.iut.org.flappydragon.View;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    public static int choixDifficulte = 1;

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
        Button reset = (Button) findViewById(R.id.reset);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButtonChecked = (RadioButton) radioGroup.findViewById(radioButtonID);
                if (radioButtonChecked != null) {
                    String selectedText = (String) radioButtonChecked.getText();
//                    Log.e("textselected",selectedText);
//                    SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putInt("selectedDifficulte", level(selectedText));
//                    editor.commit();

//                    Log.e("test", "test"+sharedPref.getInt("selectedDifficulte", 3));
//                    Log.e("test", "menu game"+sharedPref.contains("selectedDifficulte"));
//                    choixDifficulte = sharedPref.getInt("selectedDifficulte", 3);
                }
                initMenu();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
            }
        });
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

    public int level(String difficulte) {
        int level = 0;
        switch (difficulte) {
            case "Noob" :
                level = 1;
                break;
            case "Casual" :
                level = 2;
                break;
            case "PGM" :
                level = 3;
                break;
        }
        return level;
    }
}
