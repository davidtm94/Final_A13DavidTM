package com.example.a13davidtm.final_a13davidtm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;


public class Final_A13DavidTM extends Activity {
    public static final String ED_NOME = "ED_NOME";
    public static final String ED_TELEFONO = "ED_TELEFONO";
    public static final int COD_PETICION = 12;
    public static final int COD_AUDIO = 15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__a13_david_tm);
        copiarBD();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        EditText et1 = (EditText) findViewById(R.id.editText);
        EditText et2 = (EditText) findViewById(R.id.editText2);
        outState.putString(ED_NOME, et1.getText().toString());
        outState.putString(ED_TELEFONO, et2.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText et = (EditText) findViewById(R.id.editText);
        EditText et2 = (EditText) findViewById(R.id.editText2);
        et.setText(savedInstanceState.getString(ED_NOME));
        et2.setText(savedInstanceState.getString(ED_TELEFONO));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_final__a13_david_tm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        EditText ed = (EditText) findViewById(R.id.editText);

        //if(item.getItemId()==R.id)
        switch (item.getItemId()) {
            case R.id.azul:
                ed.setTextColor(Color.BLUE);
                return true;

            case R.id.verde:
                ed.setTextColor(Color.GREEN);
                return true;


        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickChamar(View view) {
        EditText edTel=(EditText) findViewById(R.id.editText2);
        String telefono=edTel.getText().toString();

        if(telefono.length()<=0){
            Toast toast=Toast.makeText(this, getString(R.string.toastChamada), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)"+telefono));
        startActivity(intent);

    }

    public void onClickSalario(View view) {
        EditText edNome=(EditText) findViewById(R.id.editText);
        String nome=edNome.getText().toString();

        if(nome.length()<=0){
            Toast toast=Toast.makeText(this, "Debes cubrir o nome", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Intent intent = new Intent(this, ProcesarSalario.class);
        intent.putExtra("NOME", edNome.getText().toString());
        startActivityForResult(intent, COD_PETICION);
    }
    //Exercicio do video
    public void onClickVideo(View view) {
        Intent intent=new Intent(this, VideoActivity.class);
        startActivity(intent);
    }

    public void onClickAudio(View view) {
        EditText edNome=(EditText) findViewById(R.id.editText);
        String nome=edNome.getText().toString();

        if(nome.length()<=0){
            Toast toast=Toast.makeText(this, "Debes cubrir o nome", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }


        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            Log.e("ERRO","TARXETA NON MONTADA");
            return;
        }
        File directorioAudio=new File(Environment.getExternalStorageDirectory(),"AUDIO");
        directorioAudio.mkdir();
        File directorioUsuario=new File(directorioAudio.getAbsolutePath(),edNome.getText().toString());
        directorioUsuario.mkdirs();
        Log.i("RUTA",directorioUsuario.getAbsolutePath());


        Intent intent=new Intent(this, AudioActivity.class);
        intent.putExtra("NOME_ED",edNome.getText().toString());
        startActivityForResult(intent, COD_AUDIO);

    }

    private void copiarBD() {
        String bddestino = "/data/data/" + getPackageName() + "/databases/"
                + "basededatos.db";
        File file = new File(bddestino);
        if (file.exists()) {
            Toast.makeText(getApplicationContext(), "A BD NON SE VAI COPIAR. XA EXISTE", Toast.LENGTH_LONG).show();
            return; // XA EXISTE A BASE DE DATOS
        }

        String pathbd = "/data/data/" + getPackageName()
                + "/databases/";
        File filepathdb = new File(pathbd);
        filepathdb.mkdirs();

        InputStream inputstream;
        try {
            inputstream = getAssets().open("basededatos.db");
            OutputStream outputstream = new FileOutputStream(bddestino);

            int tamread;
            byte[] buffer = new byte[2048];

            while ((tamread = inputstream.read(buffer)) > 0) {
                outputstream.write(buffer, 0, tamread);
            }

            inputstream.close();
            outputstream.flush();
            outputstream.close();
            Toast.makeText(getApplicationContext(), "BASE DE DATOS COPIADA", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
