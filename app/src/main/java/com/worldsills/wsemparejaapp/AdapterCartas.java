package com.worldsills.wsemparejaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class AdapterCartas extends BaseAdapter {

    private Context context;
    private int itemLayout;
    private ArrayList<ItemCarta> cartas;
    private final int[] FIGURAS={
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7,
            R.drawable.a8,


    };

    public AdapterCartas(Context context, int itemLayout, ArrayList<ItemCarta> cartas) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.cartas = cartas;
    }

    @Override
    public int getCount() {
        return cartas.size();
    }

    @Override
    public Object getItem(int position) {
        return cartas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row=view;
        Holder holder=new Holder();
        if (row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(itemLayout,null);

            holder.figura=row.findViewById(R.id.item_carta_figura);
            holder.fondoTapar=row.findViewById(R.id.item_carta_fondo_tapar);

            row.setTag(holder);

        }else{
            holder=(Holder)row.getTag();
        }
        ItemCarta carta=cartas.get(position);

        holder.figura.setBackgroundResource(FIGURAS[carta.getNumero()]);
        holder.fondoTapar.setBackgroundResource(carta.getFondoTapar());


        return row;
    }
    class Holder{
        ImageView figura, fondoTapar;
    }
}
