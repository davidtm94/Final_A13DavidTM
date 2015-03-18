package com.example.a13davidtm.final_a13davidtm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by a13davidtm on 3/13/15.
 */
public class ClaseBBDD extends SQLiteOpenHelper {

    public final static String NOME_BD="basededatos.db";
    public final static int VERSION_BD=1;





    public ClaseBBDD(Context context) {
        super(context, NOME_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
