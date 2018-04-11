package network.iut.org.flappydragon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Android on 11/04/2018.
 */

public class Option extends Activity {

    public Option(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.options);
        super.onCreate(savedInstanceState);
        Button retour = (Button) findViewById(R.id.btnRetour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("zboub");
                setContentView(R.layout.menu_game);
            }
        });
    }
}
