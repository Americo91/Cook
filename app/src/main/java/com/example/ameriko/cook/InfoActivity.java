package com.example.ameriko.cook;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Ricetta> ricette;
    String infoType;
    ProgressDialog progress;
    ListView listView;
    TextView textView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle dati = getIntent().getExtras();
        infoType = dati.getString("infoType");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ricette = new ArrayList<>();


        gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(InfoActivity.this, ricette);
        recyclerView.setAdapter(adapter);

        //inizializzo la progressBar
        progress=new ProgressDialog(this);
        progress.setMessage("Caricamento...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);

        //controllo cosa ha cliccato l'utente dal menu della MainActivity
        if(infoType.toString().equals("ricette")){
            //Toast.makeText(InfoActivity.this, "Ricette!", Toast.LENGTH_LONG).show();
            progress.show();
            contactServerRicette();
        }else if(infoType.toString().equals("istruzioni")){
            istruzioni();
            //Toast.makeText(InfoActivity.this, "istruzioni!", Toast.LENGTH_LONG).show();
        }else if(infoType.toString().equals("chisiamo")){
            //Toast.makeText(InfoActivity.this, "chisiamo!", Toast.LENGTH_LONG).show();
            chiSiamo();
        }else if(infoType.toString().equals("notelegali")){
            noteLegali();
            //Toast.makeText(InfoActivity.this, "notelegali!", Toast.LENGTH_LONG).show();
        }else finish();

    }

    public void indietro(View v){
        finish();
    }


    public void istruzioni(){
        textView = (TextView)findViewById(R.id.textViewInfo);
        textView.setVisibility(View.VISIBLE);
        String testo = "Questa Applicazione è stata realizzata con lo scopo di permettere all'utente di consulatare delle ricette, mentre cucina, senza avere un contatto fisico con il telefono.\n\n";
        testo = testo + "Dalla home, puoi scorrere la mano dalla sinistra verso la destra tramite la fotocamera frontale.\n\n";
        testo = testo + "Appena compare il microfono, potrai pronunciare il nome di una ricetta (tra quelle presenti...)\n\n";
        testo = testo + "Se la ricetta è presente, potrai iniziare a consultare una guida che ti aiuterà. Potrai andare avanti di pagina facendo scorrere la mano verso sinistra, o tornare alla pagina precedente facendola scorrere verso destra (proprio come se stessi sfogliando un libro).\n\n";
        testo = testo + "Buon divertimento....!";
        textView.setText(testo.toString());


    }

    public void noteLegali(){
        textView = (TextView)findViewById(R.id.textViewInfo);
        textView.setVisibility(View.VISIBLE);
        String testo = "Questa Applicazione è stata realizzata con lo scopo di permettere all'utente di consulatare delle ricette, mentre cucina, senza avere un contatto fisico con il telefono.\n\n";
        testo = testo + "Per far questo, vengono utilizzati la fotocamera frontale ed il microfono del dispositivo.\n\n";
        testo = testo + "Tutte le immagini e gli audio catturati dalla fotocamera e dal microfono, non sono assolutamente memorizzati o inviati a terze persone..\n\n";
        testo = testo + "...sono solo utilizzati dall'Applicazione per elaborarli e permettere quindi all'utente un'interazione con il sistema senza avere un contatto fisico con il dispositivo.";
        textView.setText(testo.toString());


    }

    public void chiSiamo(){
        textView = (TextView)findViewById(R.id.textViewInfo);
        textView.setVisibility(View.VISIBLE);
        String testo = "Christian Cardia & Americo Stoppello";
        textView.setText(testo.toString());

    }



    public void contactServerRicette(){

        listView.setVisibility(ListView.VISIBLE);

        String URL = "http://amerchri.altervista.org/Cook/listaRicette2.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.cancel();
                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());


                            if (jsonArray.length() > 0) {
                                ArrayList<Ricetta> ricette = new ArrayList<>();

                                //adesso inizializzo l'array con tutte le ricette (l'array serve per l'Array Adapter)
                                for(int i=0;i<jsonArray.length();i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    int id = jsonObject.getInt("id");
                                    String nome = jsonObject.getString("nome");
                                    String img = jsonObject.getString("img");

                                    ricette.add(new Ricetta(id,nome,img));
                                }



                            } else {
                                Toast.makeText(InfoActivity.this, "Non ci sono ricette!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }catch(Exception e){
                            Toast.makeText(InfoActivity.this, "Problema JSON con la risposta del server!", Toast.LENGTH_LONG).show();
                            finish();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.cancel();
                        //Toast.makeText(MainActivity.this, "Errore dal server:"+error.toString(), Toast.LENGTH_SHORT).show();
                        //Log.i(TAG,"onActivityResult 4  Errore nel comunicare con il Server:"+error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("ricetta",""+matches.get(0));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InfoActivity.this);
        requestQueue.add(stringRequest);


    }





}
