package com.worldsills.wsemparejaapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Home extends AppCompatActivity {

    private String player1, player2;
    private int player=0;
    Dialog scores, names, settings, dificultad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ///DIALOGS//////

        scores= new Dialog(this);
        scores.setContentView(R.layout.dialog_scores);
        scores.setCanceledOnTouchOutside(false);
        scores.setCancelable(false);
        scores.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        names= new Dialog(this);
        names.setContentView(R.layout.dialog_names);
        names.setCanceledOnTouchOutside(false);
        names.setCancelable(false);
        names.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        settings= new Dialog(this);
        settings.setContentView(R.layout.dialog_time);
        settings.setCanceledOnTouchOutside(false);
        settings.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }


    private void ingresarNombres(){

        EditText ingreso= names.findViewById(R.id.ingreso);

        if(player==1){
           ingreso.setText("Jugador 2");
        }

    }

    public void jugar(View v){

        Intent intent= new Intent(this, Game.class);
        startActivity(intent);
    }
}
