package com.izaram.agendaapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.izaram.agendaapp.R;
import com.izaram.agendaapp.adapter.RecyclerContatosAdapter;
import com.izaram.agendaapp.helper.RecyclerItemClickListener;
import com.izaram.agendaapp.model.Contato;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaContatosActivity extends AppCompatActivity {

    private ArrayList<Contato> contatoList;
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
        listAllContatos(1);


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

        //Contato Action (View, Edit or Delete)
        rvContatos.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvContatos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // View Contato
                Intent intent = new Intent(new Intent(getApplicationContext(), ViewContatoActivity.class));
                intent.putExtra("CONTATO", contatoList.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListaContatosActivity.this);
                alertBuilder.setTitle("Edit or Delete Contato");
                alertBuilder.setMessage("Choose an action");

                //Edit Contato
                alertBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), AdicionarContatoActivity.class);
                        intent.putExtra("ACTION", 0);
                        intent.putExtra("CONTATO", contatoList.get(position));
                        startActivity(intent);
                        finish();
                    }
                });

                //Delete contato
                alertBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteContatoByID(position);
                    }
                });

                alertBuilder.create();
                alertBuilder.show();


            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        // Add Contato
        ImageButton addContatoButton = findViewById(R.id.btn_adicionarContato);
        addContatoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdicionarContatoActivity.class);
                intent.putExtra("ACTION", 1);
                startActivity(intent);
                finish();
            }
        });
    }

    private void listAllContatos(int user_id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url = "http://10.0.2.2:8080/api/contatos";
        String url = "http://192.168.0.104:8000/api/contatos/" + user_id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject objContato = (JSONObject) response.get(i);
                                Contato contato = new Contato(objContato.getLong("id"),
                                        objContato.getString("nome"),
                                        objContato.getString("categoria"));

                                contatoList.add(contato);

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


    private void deleteContatoByID(int position) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.0.104:8000/api/delete/contatos/";
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                url + contatoList.get(position).getId(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                contatoList.remove(position);
                contatoList.clear();
                listAllContatos(1);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();

                searchView.clearFocus();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}