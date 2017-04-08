package com.example.ameriko.cook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;
import edu.washington.cs.touchfreelibrary.sensors.ClickSensor;
import edu.washington.cs.touchfreelibrary.utilities.LocalOpenCV;
import edu.washington.cs.touchfreelibrary.utilities.PermissionUtility;

public class RicettaActivity extends AppCompatActivity implements CameraGestureSensor.Listener, ClickSensor.Listener {

    private int numeroPagina;
    private String ricetta;

    TextView textView, textView2;
    ImageView imageView;

    //ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricetta);

        textView = (TextView)findViewById(R.id.textView);
        textView.setText("ISTRUZIONI: Sposta la mano verso l'alto per tornare alla home - Sposta la mano verso sinistra per proseguire nella ricetta!");
        textView2 = (TextView)findViewById(R.id.textView2);

        imageView = (ImageView) findViewById(R.id.imgRicetta);

        numeroPagina = 0;

        /*
        progress=new ProgressDialog(this);
        progress.setMessage("Caricamento...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        */

        Bundle dati = getIntent().getExtras();
        ricetta = dati.getString("ricetta");
        String imageUrl = "http://amerchri.altervista.org/Image/"+dati.getString("img");

        Picasso.with(this).load(imageUrl).into(imageView);

        Log.i("RicettaActivity","onCreate() 0");


        if (PermissionUtility.checkCameraPermission(this)) {
            LocalOpenCV loader = new LocalOpenCV(this,this,this);
        }

        Log.i("RicettaActivity","onCreate() 1");

        contactServer();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (PermissionUtility.checkCameraPermission(this)) {
            LocalOpenCV loader = new LocalOpenCV(this, this, this);
        }
    }


    //richiamato per farsi passare dal server la prossima pagina
    public void contactServer(){

        //progress.show();
        String URL = "http://amerchri.altervista.org/Cook/ricetta.php?ricetta="+ricetta.toString()+"&numeroPagina="+(numeroPagina+1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progress.cancel();

                        Log.i("RicettaActivity","contactServer()  Risposta Server:"+response.toString());


                        try {
                        JSONObject tmp = null;

                            tmp = new JSONObject(response.toString());


                        Log.i("RicettaActivity","2 contactServer()  Risposta Server:"+response.toString());

                        if (tmp.getString("result").equals("true")){

                            Log.i("RicettaActivity","3 contactServer()  Risposta Server:"+response.toString());

                            numeroPagina++;
                            textView2.setText(tmp.getString("testo"));
                            Log.i("RicettaActivity","ricetta="+ricetta.toString()+"&numeroPagina="+(numeroPagina));

                        }else{
                            textView2.setText("Ricetta terminata stronzo!");
                        }

                        }catch(Exception e){
                            Toast.makeText(RicettaActivity.this, "Problema JSON con la risposta del server!", Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progress.cancel();
                        //Toast.makeText(MainActivity.this, "Errore dal server:"+error.toString(), Toast.LENGTH_SHORT).show();
                        Log.i("Log","onActivityResult 4  Errore nel comunicare con il Server:"+error.toString());
                        textView.setText(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("ricetta",""+matches.get(0));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RicettaActivity.this);
        requestQueue.add(stringRequest);

    }//fine contactServer()



    //richiamato per farsi passare dal server la pagina precedente
    public void contactServerBack(){

        //progress.show();
        String URL = "http://amerchri.altervista.org/Cook/ricetta.php?ricetta="+ricetta.toString()+"&numeroPagina="+(numeroPagina-1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progress.cancel();

                        Log.i("RicettaActivity","contactServerBack()  Risposta Server:"+response.toString());


                        try {
                            JSONObject tmp = null;

                            tmp = new JSONObject(response.toString());


                            Log.i("RicettaActivity","2 contactServer()  Risposta Server:"+response.toString());

                            if (tmp.getString("result").equals("true")){

                                Log.i("RicettaActivity","3 contactServer()  Risposta Server:"+response.toString());

                                numeroPagina--;

                                //Toast.makeText(RicettaActivity.this, tmp.getString("testo"), Toast.LENGTH_LONG).show();
                                textView2.setText(tmp.getString("testo"));
                                Log.i("RicettaActivity","ricetta="+ricetta.toString()+"&numeroPagina="+(numeroPagina));

                            }else{
                                //Toast.makeText(RicettaActivity.this, "Mi spiace...La ricetta non è presente!", Toast.LENGTH_LONG).show();
                                //textView2.setText("Ricetta terminata stronzo!");
                            }

                        }catch(Exception e){
                            Toast.makeText(RicettaActivity.this, "Problema JSON con la risposta del server!", Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progress.cancel();
                        //Toast.makeText(MainActivity.this, "Errore dal server:"+error.toString(), Toast.LENGTH_SHORT).show();
                        Log.i("Log","onActivityResult 4  Errore nel comunicare con il Server:"+error.toString());
                        textView.setText(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("ricetta",""+matches.get(0));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RicettaActivity.this);
        requestQueue.add(stringRequest);

        //dialog.dismiss();
    }



    //metodi che gestiscono i gesture
    @Override
    public void onSensorClick(ClickSensor caller) {

    }


    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RicettaActivity.this, "Ti sei mosso verso l'alto!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RicettaActivity.this, "Ti sei mosso verso il basso!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RicettaActivity.this, "Ti sei mosso verso sinistra!", Toast.LENGTH_SHORT).show();
                contactServer();
            }
        });
    }

    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RicettaActivity.this, "Ti sei mosso verso sinistra!", Toast.LENGTH_SHORT).show();
                contactServerBack();
            }
        });

    }
}//fine contactServer

