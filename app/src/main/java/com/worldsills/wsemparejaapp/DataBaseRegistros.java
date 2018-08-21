package com.worldsills.wsemparejaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseRegistros extends SQLiteOpenHelper {

    private static final String NOMBRE="registros.db";
    private static final int VERSION=1;
    private static final String TABLA="CREATE TABLE REGISTRO(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "NOMBRE TEXT, PUNTAJE INTEGER, TIEMPO INTEGER, DIFICULTAD TEXT)";

    DataBaseRegistros (Context context){
        super(context,NOMBRE,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CREATE"+TABLA);
        sqLiteDatabase.execSQL(TABLA);
    }
    public void guardarDatos(String nombre, int puntaje, int tiempo, String dificultad){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues valores=new ContentValues();
        valores.put("NOMBRE",nombre);
        valores.put("PUNTAJE",puntaje);
        valores.put("TIEMPO",tiempo);
        valores.put("DIFICULTAD",dificultad);

        db.insert("REGISTRO",null, valores);
    }
    public Cursor cargarDatos(String dificultad){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=null;

        try{
            String[] columns={"NOMBRE","PUNTAJE"};
            String selection="DIFICULTAD" + " =? ";
            String[] selectionArgs={dificultad};
            String limit="5";
            String orderBy="PUNTAJE DESC";

            cursor=db.query("REGISTRO",columns,selection,selectionArgs,null,null,orderBy,limit);

        }catch (Exception e){}

        return cursor;
    }
}
