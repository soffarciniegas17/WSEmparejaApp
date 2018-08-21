package com.worldsills.wsemparejaapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    private String player1, player2;
    private int player=0, tiempo;
    private String puntajes[], mododif;
    Dialog scores, names, settings, dificultad;
    TextView p1, p2, p3, p4, p5, tp1, tp2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        tp1= findViewById(R.id.tp1);
        tp2= findViewById(R.id.tp2);

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


        dificultad= new Dialog(this);
        dificultad.setContentView(R.layout.dialog_dificultad);
        dificultad.setCanceledOnTouchOutside(false);
        dificultad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ingresarNombres();
    }


    private void ingresarNombres(){
        final EditText ingreso= names.findViewById(R.id.ingreso);
        Button confirmar= names.findViewById(R.id.confirmar);

        if(player==1){
           ingreso.setText("Jugador 2");

        }

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player==0){
                    player1=ingreso.getText().toString();
                    player=1;

                    tp1.setText(player1);
                    ingresarNombres();
                } else {
                    player2= ingreso.getText().toString();
                    tp2.setText(player2);
                    names.dismiss();
                }
            }
        });

        names.show();

    }

    public void jugar(View v){

        Button b1, b2, b3;
         b1= findViewById(R.id.ba);
         b2= findViewById(R.id.bb);
         b3= findViewById(R.id.bc);


         b1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               Intent  i= new Intent(Home.this, Game.class);
               i.putExtra("player1", player1);
               i.putExtra("player2", player2);
               i.putExtra("dificultad", 4);
               startActivity(i);
               finish();
             }
         });

         b2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent  i= new Intent(Home.this, Game.class);
                 i.putExtra("player1", player1);
                 i.putExtra("player2", player2);
                 i.putExtra("dificultad", 6);
                 startActivity(i);
                 finish();
             }
         });

         b3.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent  i= new Intent(Home.this, Game.class);
                 i.putExtra("player1", player1);
                 i.putExtra("player2", player2);
                 i.putExtra("dificultad", 8);
                 startActivity(i);
                 finish();
             }
         });

    }


    public void ScoresShow(View v){
        p1= scores.findViewById(R.id.p1);
        p2= scores.findViewById(R.id.p2);
        p3= scores.findViewById(R.id.p3);
        p4= scores.findViewById(R.id.p4);
        p5= scores.findViewById(R.id.p5);


        mododif="4";

        puntajes= new String[5];
        defaultScores();
       // ConsultaDedatos();
        organizar();

        Button salir = scores.findViewById(R.id.cancel);

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores.dismiss();
            }
        });
    }

    private void defaultScores(){
        for (int i=0; i<puntajes.length; i++){
            if(i==0){
                puntajes[i]= "Jugador \n 0";
            } else {
                puntajes[i]= (i+1)+ ".    Jugador     0";
            }
        }
    }


  /*  private void ConsultaDeDatos(){
        DataBase db  = new DataBase(this);
        Cursor cursor= db.cargar("");
        int i=0;
        if(cursor.moveToFirst()) {

            do {
                if(i==0){
                    puntajes[i]= cursor.getString(0)+"   " +cursor.getInt(0);
                } else {
                    puntajes[i]= (i+1)+ ".    Jugador     0";
                }
                i++;
            }while (cursor.moveToNext());
        }
    }*/
    private void organizar(){
        p1.setText(puntajes[0]);
        p2.setText(puntajes[1]);
        p3.setText(puntajes[2]);
        p4.setText(puntajes[3]);
        p5.setText(puntajes[4]);

    }

    public void ScoresButtons(View v){
        switch (v.getId()){
            case R.id.beasy:
                mododif="4";

                defaultScores();
                //ConsultaDeDatos();
                organizar();
                break;
            case R.id.bmedio:

                mododif="6";

                defaultScores();
                //ConsultaDeDatos();
                organizar();
                break;
            case R.id.bdificil:
                mododif="8";

                defaultScores();
                //ConsultaDeDatos();
                organizar();

                break;
        }
    }

    private boolean modo;
    public void configuracion(View v){
       final  EditText tiempoingreso = settings.findViewById(R.id.time);
       final TextView estado = settings.findViewById(R.id.estado);


        Button activado, desactivado;
        activado= settings.findViewById(R.id.activadd);
        desactivado= settings.findViewById(R.id.desactivado);

        activado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo=true;
                tiempo= Integer.parseInt(tiempoingreso.getText().toString());
                estado.setText("Modo tiempo ACTIVADO");
                settings.dismiss();
            }
        });


        desactivado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo= false;
                estado.setText("Modo tiempo ACTIVADO");
                settings.dismiss();
            }
        });

    }


    ///////////////// Activity metodos///////////////////


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putInt("tiempo", tiempo);
        editor.putBoolean("modo", modo);


        editor.apply();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        modo= sharedPreferences.getBoolean("modo", false);
        tiempo= sharedPreferences.getInt("tiempo", 0);

    }
}
