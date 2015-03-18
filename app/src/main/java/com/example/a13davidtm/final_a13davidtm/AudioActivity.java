package com.example.a13davidtm.final_a13davidtm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AudioActivity extends Activity {
    public String nome;
    private MediaRecorder mediaRecorder;
    private String arquivoGravar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Intent intent = getIntent();
        nome=intent.getExtras().getString("NOME_ED");
        cargaSpinner();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cargaSpinner() {

        File directorioAudio=new File(Environment.getExternalStorageDirectory(),"AUDIO");
        File directorioUsuario=new File(directorioAudio.getAbsolutePath(),nome);
        File[] array=directorioUsuario.listFiles();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,array);
        Spinner spinner=(Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
    }

    public void onReproducir(View view) {
        Spinner spinner=(Spinner)findViewById(R.id.spinner);
        File arquivo=(File)spinner.getSelectedItem();
            if (!arquivo.exists()) return;

            final MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(arquivoGravar);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "ERRO:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
    }

    public void onParar(View view) {
    mediaRecorder.stop();
                                    mediaRecorder.release();
                                    mediaRecorder = null;
    }

    public void onGravar(View view) {
    // TODO Auto-generated method stub
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                File directorioAudio=new File(Environment.getExternalStorageDirectory(),"AUDIO");
                File directorioUsuario=new File(directorioAudio.getAbsolutePath(),nome);

                mediaRecorder = new MediaRecorder();
                arquivoGravar = directorioUsuario.getAbsolutePath() + "record_"+timeStamp + ".3gp";
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setMaxDuration(10000);
                mediaRecorder.setAudioEncodingBitRate(32768);
                mediaRecorder.setAudioSamplingRate(8000); // No emulador só 8000 coma
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setOutputFile(arquivoGravar);
                try {
                    mediaRecorder.prepare();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    mediaRecorder.reset();
                }
                mediaRecorder.start();
    }

}
