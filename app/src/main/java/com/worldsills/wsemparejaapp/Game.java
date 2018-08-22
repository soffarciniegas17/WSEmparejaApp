package com.worldsills.wsemparejaapp;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {

    private ArrayList<ItemCarta> cartas;
    private AdapterCartas adapter;
    private GridView gridView;

    private View view1, view2;
    private int posicion1, posicion2;
    private boolean turno, clickCarta, temporizadorActivado;

    private TextView viewJugador1, viewJugador2, viewPuntaje1, viewPuntaje2, viewTemporizador;
    private Chronometer viewChronometer;

    private int puntaje1, puntaje2, tiempoPartida, dificultad;
    private Long tiempoTemporizador;
    private String nomJugador1, nomJugador2;


    private final int TAPAR=R.drawable.fondo_tapar_item_carta;
    private final int DESTAPAR=android.R.color.transparent;

    private Animation voltear, desaparecer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Bundle datos=getIntent().getExtras();
        if(datos!=null){
            dificultad=(datos.getInt("dificultad"))*2;
            nomJugador1=datos.getString("player1");
            nomJugador2=datos.getString("player2");
        }else{
            dificultad=8;

        }
        puntaje1=0;
        puntaje2=0;
        tiempoPartida=0;
        turno=true;
        clickCarta=true;
        findViews();
        cargaAnimaciones();



    }
    //Metodo para inicializar las vistas
    public void findViews(){
        gridView=findViewById(R.id.gridview);
        viewJugador1=findViewById(R.id.game_jugador1);
        viewJugador2=findViewById(R.id.game_jugador2);
        viewPuntaje1=findViewById(R.id.game_puntaje1);
        viewPuntaje2=findViewById(R.id.game_puntaje2);
        viewChronometer=findViewById(R.id.game_chronometer);
        viewTemporizador=findViewById(R.id.game_temporizador);

    }
    public void cargaAnimaciones(){
        voltear= AnimationUtils.loadAnimation(this,R.anim.voltear);
        voltear.setFillAfter(true);
        desaparecer=AnimationUtils.loadAnimation(this,R.anim.desaparecer);
        desaparecer.setFillAfter(true);

    }
    //Metodo para organizar las cartas en el adapter
    public void cargaCartas(){
        try{
            cartas=new ArrayList<>();
            adapter=new AdapterCartas(this,R.layout.item_carta,cartas);
            gridView.setAdapter(adapter);

            organizaFigurasAzar(dificultad/2);


        }catch (Exception e){}

        adapter.notifyDataSetChanged();
        clickCartas();
    }
    //Metodo encargador de colocar las figuras al azar
    public void organizaFigurasAzar(int parejas){
        int[] numeros=new int[dificultad];
        for (int i=0; i<numeros.length; i++)numeros[i]=-1;

        int base=0;
        boolean comprueba;

        do{
            int position=new Random().nextInt(numeros.length);

            if (numeros[position]==-1){
                numeros[position]=base;
                base++;

                if (base==parejas)base=0;

            }

            comprueba=false;
            for (int i=0; i<numeros.length;i++)if (numeros[i]==-1)comprueba=true;
        }while (comprueba);

        for (int i=0; i<dificultad;i++)cartas.add(new ItemCarta(numeros[i],TAPAR));

    }
    //Metodo para poner a la escucha cada item carta precionada y hacer su respectiva acción
    public void clickCartas(){


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (clickCarta){

                    if (view1!=null && view2!=null){
                        view1.clearAnimation();
                        view2.clearAnimation();
                    }
                    posicion1=position;
                    view1=view;

                    view1.startAnimation(voltear);

                    cartas.get(posicion1).setFondoTapar(DESTAPAR);

                    clickCarta=false;
                }else if(cartas.get(position).getFondoTapar()==TAPAR){

                    posicion2=position;
                    view2=view;

                    view1.clearAnimation();
                     view2.startAnimation(voltear);

                    cartas.get(posicion2).setFondoTapar(DESTAPAR);

                    clickCarta=true;
                    compruebaCartasPresionadas();
                }

                adapter.notifyDataSetChanged();

            }
        });

    }
    //Metodo para verificar que las cartas escogidas por el usuario son correctas o incorrectas;
    private CountDownTimer timerEspera, timerPartida;
    public void compruebaCartasPresionadas(){
        gridView.setOnItemClickListener(null);

        timerEspera=new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (cartas.get(posicion1).getNumero()==cartas.get(posicion2).getNumero()){

                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.INVISIBLE);

                    view1.startAnimation(desaparecer);
                    view2.startAnimation(desaparecer);

                    if (turno){
                        puntaje1+=100;

                    }else{
                        puntaje2+=100;

                    }


                }else{

                    cartas.get(posicion1).setFondoTapar(TAPAR);
                    cartas.get(posicion2).setFondoTapar(TAPAR);

                    view1.startAnimation(voltear);
                    view1.startAnimation(voltear);

                    if (turno){
                        puntaje1-=2;
                        turno=false;
                    }else{
                        puntaje2-=2;
                        turno=true;
                    }
                }

                adapter.notifyDataSetChanged();
                actualizarPantalla();
                if (verificaPartida())clickCartas();
                else finPartida();

            }
        }.start();
    }
    public void actualizarPantalla(){
        viewJugador1.setText(nomJugador1);
        viewJugador2.setText(nomJugador2);
        viewPuntaje1.setText(puntaje1+"");
        viewPuntaje2.setText(puntaje2+"");

        if(turno){
            viewJugador1.setBackgroundResource(R.color.negro_claro);
            viewPuntaje1.setBackgroundResource(R.color.negro_claro);

            viewJugador2.setBackgroundResource(R.color.gris_oscuro);
            viewPuntaje2.setBackgroundResource(R.color.gris_oscuro);
        }else{
            viewJugador2.setBackgroundResource(R.color.negro_claro);
            viewPuntaje2.setBackgroundResource(R.color.negro_claro);

            viewJugador1.setBackgroundResource(R.color.gris_oscuro);
            viewPuntaje1.setBackgroundResource(R.color.gris_oscuro);

        }
    }
    //Metodo para verificar si ya todas las cartas han desaparedido de la pantalla
    public boolean verificaPartida(){

        for (int i=0; i<cartas.size(); i++)if (cartas.get(i).getFondoTapar()==TAPAR)return true;

        return false;
    }

    public void finPartida(){
        try{
            timerEspera.cancel();
        }catch (Exception e){}
        try{
            timerPartida.cancel();
        }catch (Exception e){}


        if (!temporizadorActivado)tiempoPartida=(int)(viewChronometer.getBase()- SystemClock.elapsedRealtime())/-1000;
        abreDiaglogFinal();
        guardarDatos();
    }

    public void abreDiaglogFinal(){
        Dialog dialogFinal=new Dialog(this);
        dialogFinal.setContentView(R.layout.dialog_final);
        dialogFinal.setCanceledOnTouchOutside(false);

        TextView puntaje, tiempo, nombre;
        puntaje=dialogFinal.findViewById(R.id.final_puntaje);
        tiempo=dialogFinal.findViewById(R.id.final_tiempo);
        nombre=dialogFinal.findViewById(R.id.final_nombre_jugador);


            if (puntaje1 > puntaje2) {
                puntaje.setText(puntaje1 + "");
                nombre.setText(nomJugador1);
            }
            else {
                puntaje.setText(puntaje2+"");
                nombre.setText(nomJugador2);
            }

            tiempo.setText(tiempoPartida+"");

            dialogFinal.show();

    }
    public void guardarDatos(){
        DataBaseRegistros db=new DataBaseRegistros(this);
        db.guardarDatos(nomJugador1,puntaje1,tiempoPartida,dificultad/2+"");
        db.guardarDatos(nomJugador2,puntaje2,tiempoPartida,dificultad/2+"");
    }
    public void finalBotones(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.fin_bton_home:
                intent=new Intent(this,Home.class);
                startActivity(intent);
                finish();
                break;
            case R.id.fin_bton_compartir:
                break;
            case R.id.fin_bton_replay:
                intent=new Intent(this,Game.class);
                intent.putExtra("",dificultad/2);
                finish();
                break;
        }

    }
    public void timerPartida(){
        timerPartida=new CountDownTimer(tiempoTemporizador,1000) {
            @Override
            public void onTick(long l) {
                tiempoPartida=(int)l/1000;
                if (tiempoPartida<9)viewTemporizador.setText("0"+tiempoPartida);
                else viewTemporizador.setText(""+tiempoPartida);
            }

            @Override
            public void onFinish() {
                finPartida();
            }
        }.start();
    }


    public void onResume(){
        super.onResume();

        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);

        temporizadorActivado=datos.getBoolean("modo",false);
        tiempoTemporizador=(long)(datos.getInt("tiempo",30))*1000;

        if (temporizadorActivado){
           viewChronometer.getLayoutParams().height=0;
            timerPartida();
        }else{
            viewChronometer.start();
        }

        turno=new Random().nextBoolean();
        actualizarPantalla();
        cargaCartas();
    }
    public void onBackPressed(){
        Intent intent=new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

}
