package com.worldsills.wsemparejaapp;

import android.app.Dialog;
import android.content.ClipData;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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



    private final int TAPAR=R.drawable.fondo_tapar_item_carta;
    private final int DESTAPAR=android.R.color.transparent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle datos=getIntent().getExtras();
        if(datos!=null){
            dificultad=datos.getInt("");
        }else{
            dificultad=8;

        }
        puntaje1=0;
        puntaje2=0;
        tiempoPartida=0;
        turno=true;
        clickCarta=true;
        findViews();



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

                    posicion1=position;
                    view1=view;

                    cartas.get(posicion1).setFondoTapar(DESTAPAR);

                    clickCarta=false;
                }else if(cartas.get(position).getFondoTapar()==TAPAR){

                    posicion2=position;
                    view2=view;

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

                    if (turno){
                        puntaje1+=100;
                    }else{
                        puntaje2+=100;
                    }


                }else{

                    cartas.get(posicion1).setFondoTapar(TAPAR);
                    cartas.get(posicion2).setFondoTapar(TAPAR);

                    if (turno){
                        puntaje1-=2;
                        turno=false;
                    }else{
                        puntaje1-=2;
                        turno=true;
                    }
                }

                adapter.notifyDataSetChanged();

                if (verificaPartida())clickCartas();
                else finPartida();

            }
        }.start();
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

        TextView puntaje, tiempo;
        puntaje=dialogFinal.findViewById(R.id.final_puntaje);
        tiempo=dialogFinal.findViewById(R.id.final_tiempo);


            if (puntaje1 > puntaje2) puntaje.setText(puntaje1 + "");
            else puntaje.setText(puntaje2+"");

            tiempo.setText(tiempoPartida);

            dialogFinal.show();

    }
    public void guardarDatos(){

    }
    public void finalBotones(View v){
        switch (v.getId()){
            case R.id.fin_bton_home:
                break;
            case R.id.fin_bton_compartir:
                break;
            case R.id.fin_bton_replay:
                break;
        }

    }

    public void onResume(){
        super.onResume();

        temporizadorActivado=false;
        cargaCartas();
    }

}
