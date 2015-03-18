package com.example.a13davidtm.final_a13davidtm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VideoActivity extends Activity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView videoView;
    public static String rutaUltimoVideo="";
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView=(VideoView)findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));
        tv=(TextView)findViewById(R.id.textView2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
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

    public void lanzarVideo(View view) {
        //Comprobamos que a SDCARD NON está en modo escritura, si é certo, saimos do metodo e emitimos un toast
        if (!isExternalStorageWritable()){
            Toast.makeText(this, "A SDCARD non está en modo escritura", Toast.LENGTH_SHORT).show();
            return;
        }
        //Obtemos o directorio
        File directorio = getExternalFilesDir(Environment.DIRECTORY_MOVIES); //Ruta directorio
        //Obter Uri do arquivo
        Uri uriDoArquivo=obterUriArquivo(directorio);
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriDoArquivo); //Garda o arquivo
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            //Neste punto o video xa foi capturado e gardado no directorio. Avisamos ao usuario
            videoView.setVideoURI(Uri.fromFile(new File(rutaUltimoVideo)));
            if(rutaUltimoVideo.length()>1) {
                Toast.makeText(this, "O video foi grabado e foi gardado en "+rutaUltimoVideo, Toast.LENGTH_SHORT);
            }

        }
    }

    /** Create a file Uri for saving an image or video */
    private Uri obterUriArquivo(File mediaStorageDir){
        File arquivo=obterArquivo(mediaStorageDir);
        rutaUltimoVideo=arquivo.getAbsolutePath();
        return Uri.fromFile(arquivo);
    }

    /** Create a File for saving an image or video */
    private static File obterArquivo(File mediaStorageDir){
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("ERRO", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");


        return mediaFile;
    }
    
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
