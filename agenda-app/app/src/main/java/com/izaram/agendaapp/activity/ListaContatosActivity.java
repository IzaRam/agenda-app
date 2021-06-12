package com.izaram.agendaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.izaram.agendaapp.R;
import com.izaram.agendaapp.adapter.RecyclerContatosAdapter;
import com.izaram.agendaapp.model.Contato;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaContatosActivity extends AppCompatActivity {

    // https://viacep.com.br/ws/01153-000/json/

    private ArrayList<String> contatoList;
    private RecyclerContatosAdapter rvContatosAdapter;
    private RecyclerView rvContatos;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);

        rvContatos = findViewById(R.id.rv_listaContatos);
        contatoList = new ArrayList<>();


        //List all contatos
        listAllGames();


        //Search Contato
        searchView = findViewById(R.id.search);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rvContatosAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvContatosAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // Add Contato
        FloatingActionButton addContatoButton = findViewById(R.id.btn_adicionarContato);
        addContatoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdicionarContatoActivity.class));
            }
        });
    }


    private void listAllGames() {
        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "http://10.0.2.2:8080/api/games";
        String url = "http://192.168.0.104:8000/api/contatos";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject objContato = (JSONObject) response.get(i);

                                Contato contato = new Contato(String.valueOf(objContato.getString("nome")),
                                        objContato.getString("categoria"));

                                contatoList.add(contato.getNome());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        rvContatos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rvContatosAdapter = new RecyclerContatosAdapter(getApplicationContext(), contatoList);
                        rvContatos.setAdapter(rvContatosAdapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error: ", error.toString());
                    }
                });

        queue.add(jsonArrayRequest);
    }
}