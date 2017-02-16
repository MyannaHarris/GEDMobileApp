package com.gedappgui.gedappgui;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TowerGameView extends AppCompatActivity {

    private String test_abs = "|-4| - 2 = 2,t,|-2| = 3,f,|4| - |-4| = 4,f,-5 + |20| = 15,t";
    private int curr_statement = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_game_view);

        Button truth_btn = (Button)findViewById(R.id.true_btn);
        truth_btn.getBackground().setColorFilter(0xFF23c438, PorterDuff.Mode.MULTIPLY);

        Button false_btn = (Button)findViewById(R.id.false_btn);
        false_btn.getBackground().setColorFilter(0xFFce4257, PorterDuff.Mode.MULTIPLY);

        TextView statement = (TextView) findViewById(R.id.statement);
        String[] statements = test_abs.split(",");
        statement.setText(statements[curr_statement]);

    }

    public void seeifFalse(View view){

    }

    public void seeifTrue(View view){

    }
}
