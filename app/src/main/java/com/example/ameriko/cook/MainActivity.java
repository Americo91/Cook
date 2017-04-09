package com.example.ameriko.cook;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;
import edu.washington.cs.touchfreelibrary.sensors.ClickSensor;
import edu.washington.cs.touchfreelibrary.utilities.LocalOpenCV;
import edu.washington.cs.touchfreelibrary.utilities.PermissionUtility;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CameraGestureSensor.Listener, ClickSensor.Listener {

    public final String TAG = "MainActivity LOG";
    private static final int REQUEST_CODE = 1234;

    //ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (PermissionUtility.checkCameraPermission(this)) {
            LocalOpenCV loader = new LocalOpenCV(this,this,this);
        }

        /*
        progress=new ProgressDialog(this);
        progress.setMessage("Caricamento...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        */


        //riguarda il menù
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }//fine onCreate()


    @Override
    public void onResume() {
        super.onResume();
        if (PermissionUtility.checkCameraPermission(this)) {
            LocalOpenCV loader = new LocalOpenCV(this, this, this);
        }
    }

    //tutto riguardo il Navigation View (compare in automatico quando crei l'Activity con Navigation)----------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        //intent.putExtra("ricetta", "" + matches.get(0));


        if (id == R.id.nav_ricette) {
            // Handle the camera action
            intent.putExtra("infoType", "ricette");
        } else if (id == R.id.nav_istruzioni){
            intent.putExtra("infoType", "istruzioni");
        } else if (id == R.id.nav_chisiamo) {
            intent.putExtra("infoType", "chisiamo");
        } else if (id == R.id.nav_notelegali) {
            intent.putExtra("infoType", "notelegali");
        }else{

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        return true;
    }

    //---------------------------------------------------------------fine Navigation View




    //gestione delle gesture con i rispettivi metodi
    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Up");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Ti sei mosso verso l'alto!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Down");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Ti sei mosso verso il basso!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Left");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Ti sei mosso verso sinistra!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "RIght");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Ti sei mosso verso destra!", Toast.LENGTH_SHORT).show();
                //speechStart(null);
            }
        });
    }

    @Override
    public void onSensorClick(ClickSensor caller) {
        Log.i(TAG, "Click");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "CLICK!", Toast.LENGTH_SHORT).show();
                speechStart(null);
            }
        });
    }




    //viene richiamato per far comparire la View per il riconoscimento vocale (nel nostro caso richiamato quando sposti la mano verso destra)
    public void speechStart(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    //richiamato dal metodo precedente - una volta acquisita la parola pronunciata, questo metodo contatta il server per richiedere se esiste la ricetta
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //progress.show();
        Log.i(TAG,"onActivityResult 1");
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            Log.i(TAG,"onActivityResult 2 "+matches.get(0));
            //-----------qui ho la stringa che ha detto l'utente in matches.get(0) --inizio volley
            String URL = "http://amerchri.altervista.org/Cook/ricettaImg.php?ricetta="+matches.get(0);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //progress.cancel();

                            Log.i("Log","onActivityResult 3  Risposta Server:"+response.toString());

                            try {
                                JSONObject tmp = new JSONObject(response.toString());

                                if (tmp.getString("result").equals("true")) {

                                    Intent intent = new Intent(getApplicationContext(), RicettaActivity.class);
                                    intent.putExtra("ricetta", "" + matches.get(0));
                                    intent.putExtra("img",tmp.getString("img"));
                                    startActivity(intent);
                                    //Toast.makeText(MainActivity.this, "Ricetta presente!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Mi spiace...La ricetta non è presente!", Toast.LENGTH_LONG).show();
                                }

                            }catch(Exception e){
                                Toast.makeText(MainActivity.this, "Problema JSON con la risposta del server!", Toast.LENGTH_LONG).show();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //progress.cancel();
                            Toast.makeText(MainActivity.this, "Errore dal server:"+error.toString(), Toast.LENGTH_SHORT).show();
                            Log.i(TAG,"onActivityResult 4  Errore nel comunicare con il Server:"+error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    //params.put("ricetta",""+matches.get(0));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);


        }

        //----------fine volley
        else{
            Toast.makeText(MainActivity.this, "Mi spiace..Non ho capito cosa hai detto..Riprova!", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
