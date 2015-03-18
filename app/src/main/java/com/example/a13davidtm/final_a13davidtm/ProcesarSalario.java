package com.example.a13davidtm.final_a13davidtm;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ProcesarSalario extends Activity {
    ArrayList<Salary> salaries = new ArrayList<Salary>();
    ClaseBBDD clasebd;
    String nomeED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procesar_salario);
        clasebd = new ClaseBBDD(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_procesar_salario, menu);
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

    public void showSalaries(View view) {
        TextView tv = (TextView) findViewById(R.id.textView);
        String cadea = "Total_Salary      Month\n";
        SQLiteDatabase db = clasebd.getReadableDatabase();

        Cursor cursor = db.rawQuery("select month,total_salary from salary", null);
        if (cursor.moveToFirst()) {                // Se non ten datos xa non entra
            while (!cursor.isAfterLast()) {     // Quédase no bucle ata que remata de percorrer o cursor. Fixarse que leva un ! (not) diante
                String month = cursor.getString(0);
                double total_salary = cursor.getDouble(1);
                cadea += total_salary + "               " + month + "\n";
                cursor.moveToNext();
            }
        }
        tv.setText(cadea);

    }

    public void salariesToFile(View view) {
/*
        File arquivo = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), nomeED);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivo)));
            Log.i("CHEGA9","CHEGA");
            String cadea = "Total_Salary      Month";
            bw.write(cadea);
            bw.newLine();
            SQLiteDatabase db = clasebd.getReadableDatabase();
            Log.i("CHEGA10","CHEGA");
            Cursor cursor = db.rawQuery("select month,total_salary from salary", null);
            if (cursor.moveToFirst()) {                // Se non ten datos xa non entra
                while (!cursor.isAfterLast()) {     // Quédase no bucle ata que remata de percorrer o cursor. Fixarse que leva un ! (not) diante
                    String month = cursor.getString(0);
                    double total_salary = cursor.getDouble(1);
                    bw.write(total_salary+ "               " + month);
                    bw.newLine();
                    cursor.moveToNext();
                }
            }
            Log.i("CHEGA11","CHEGA");
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Final_A13DavidTM.COD_PETICION) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("NOME"))
                    nomeED = data.getExtras().getString("NOME");

            }
        }

    }

    class Salary {
        String month;
        double total_salary;

        Salary() {
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public double getTotal_salary() {
            return total_salary;
        }

        public void setTotal_salary(double total_salary) {
            this.total_salary = total_salary;
        }
    }


    public void procesaXML(View view) {
        Log.i("CHEGA1", "CHEGA ATA AQUI");
        descargaXML();


    }

    private void gardarNaBBDD() {
        // Gets the data repository in write mode
        SQLiteDatabase db = clasebd.getWritableDatabase();

        for (Salary salarios : salaries) {

            ContentValues values = new ContentValues();
            values.put("month", salarios.getMonth());

            values.put("total_salary", salarios.getTotal_salary());
            try {
                db.insert(
                        "salary",
                        null,
                        values);
            } catch (SQLiteConstraintException ex) {
                Log.i("EXISTE", "XA EXISTE NA BD"); //SALTA A EXCEPCION CANDO SE TRATA DE INTRODUCIR UNHA
                //PRIMARY KEY QUE XA EXISTA
            }
        }
        Log.i("CHEGAFIN", "CHEGA");
    }

    private void lerArquivo() throws IOException, XmlPullParserException {

        InputStream is = openFileInput("Salaries.xml");

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");

        int evento = parser.nextTag();
        Salary salary = null;

        while (evento != XmlPullParser.END_DOCUMENT) {
            if (evento == XmlPullParser.START_TAG) {
                if (parser.getName().equals("salary")) {      // Un novo salario
                    salary = new Salary();
                    evento = parser.nextTag();      // Pasamos a <month>
                    salary.setMonth(parser.nextText());
                    evento = parser.nextTag();      // Pasamos a <amount>
                    double amount = Integer.parseInt(parser.nextText());
                    evento = parser.nextTag();      // Pasamos a <complement>
                    double comp1 = Integer.parseInt(parser.nextText());
                    evento = parser.nextTag();      // Pasamos a <complement>
                    double comp2 = Integer.parseInt(parser.nextText());
                    salary.setTotal_salary(amount + comp1 + comp2);
                }
            }
            if (evento == XmlPullParser.END_TAG) {
                if (parser.getName().equals("salary")) {      // Un novo salario
                    salaries.add(salary);
                }
            }

            evento = parser.next();
        }

        is.close();
        gardarNaBBDD();
    }


    private void descargaXML() {
        String stringUrl = "http://manuais.iessanclemente.net/images/5/53/Salaries.xml";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            Toast.makeText(this, "Non hai conexion", Toast.LENGTH_SHORT).show();
        }


    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i("CHEGA6", "CHEGA");
            //Mandamos procesar o arquvo
            try {
                lerArquivo();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        }

        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("DEBUG", "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader br = new BufferedReader(reader);
            OutputStream out = openFileOutput("Salaries.xml", MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            String texto, resultado = "";
            while ((texto = br.readLine()) != null) {
                bw.write(texto);
                bw.newLine();
            }
            br.close();
            bw.close();
            Log.i("CHEGA5", "CHEGA ATA AQUI");
            return resultado;
        }
    }
}
