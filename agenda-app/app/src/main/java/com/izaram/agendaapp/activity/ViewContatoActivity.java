package com.izaram.agendaapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.izaram.agendaapp.MainActivity;
import com.izaram.agendaapp.R;
import com.izaram.agendaapp.adapter.RecyclerContatosAdapter;
import com.izaram.agendaapp.adapter.RecyclerEnderecosAdapter;
import com.izaram.agendaapp.adapter.RecyclerTelefonesAdapter;
import com.izaram.agendaapp.helper.RecyclerItemClickListener;
import com.izaram.agendaapp.model.Contato;
import com.izaram.agendaapp.model.Endereco;
import com.izaram.agendaapp.model.Telefone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewContatoActivity extends AppCompatActivity {

    ArrayList<Telefone> telefoneList;
    ArrayList<Endereco> enderecoList;

    private RecyclerTelefonesAdapter rvTelefonesAdapter;
    private RecyclerView rvTelefones;

    private RecyclerEnderecosAdapter rvEnderecosAdapter;
    private RecyclerView rvEnderecos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contato);


        // Visualizar contato detalhes
        Contato contato = (Contato) getIntent().getSerializableExtra("CONTATO");
        TextView textViewNome = findViewById(R.id.tv_contato_nome);
        TextView textViewCategotia = findViewById(R.id.tv_contato_categoria);
        rvEnderecos = findViewById(R.id.rv_enderecos);
        rvTelefones = findViewById(R.id.rv_telefones);
        telefoneList = new ArrayList<>();
        enderecoList = new ArrayList<>();
        textViewNome.setText(contato.getNome());
        textViewCategotia.setText(contato.getCategoria());
        getContatoDetails(contato.getId());


        // Adicionar novo telefone ao contato
        FloatingActionButton btnAddTelefone = findViewById(R.id.btn_add_telefone);
        btnAddTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTelefoneActivity.class);
                intent.putExtra("CONTATO", contato);
                intent.putExtra("ACTION", 1);
                startActivity(intent);
                finish();
            }
        });

        //Telefone Action (Edit or Delete)
        rvTelefones.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvTelefones, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) { }

            @Override
            public void onLongItemClick(View view, int position) {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ViewContatoActivity.this);
                alertBuilder.setTitle("Edit or Delete Telefone");
                alertBuilder.setMessage("Choose an action");

                //Edit Telefone
                alertBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), AdicionarTelefoneActivity.class);
                        intent.putExtra("ACTION", 0);
                        intent.putExtra("TELEFONE", telefoneList.get(position));
                        intent.putExtra("CONTATO", contato);
                        startActivity(intent);
                        finish();
                    }
                });

                //Delete Telefone
                alertBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTelefoneById(position, contato.getId());
                    }
                });

                alertBuilder.create();
                alertBuilder.show();
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { }
        }));


        // Adicionar novo endereço ao contato
        FloatingActionButton btnAddEndereco = findViewById(R.id.btn_add_endereco);
        btnAddEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdicionarEnderecoActivity.class);
                intent.putExtra("CONTATO", contato);
                intent.putExtra("ACTION", 1);
                startActivity(intent);
                finish();
            }
        });

        //Endereco Action (View, Edit or Delete)
        rvEnderecos.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvEnderecos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // View Endereco
                Intent intent = new Intent(new Intent(getApplicationContext(), ViewEnderecoActivity.class));
                intent.putExtra("ENDERECO", enderecoList.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ViewContatoActivity.this);
                alertBuilder.setTitle("Edit or Delete Endereço");
                alertBuilder.setMessage("Choose an action");

                //Edit Endereco
                alertBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), AdicionarEnderecoActivity.class);
                        intent.putExtra("ACTION", 0);
                        intent.putExtra("ENDERECO", enderecoList.get(position));
                        intent.putExtra("CONTATO", contato);
                        startActivity(intent);
                        finish();
                    }
                });

                //Delete Endereco
                alertBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEnderecoById(position, contato.getId());
                    }
                });

                alertBuilder.create();
                alertBuilder.show();
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { }
        }));

    }

    private void getContatoDetails(long contato_id) {

        RequestQueue queue = Volley.newRequestQueue(this);
        //String url = "http://10.0.2.2:8080/api/contatos";
        String url = "http://192.168.0.104:8000/api/show/contatos/" + contato_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray telefones = response.getJSONArray("telefones");
                            JSONArray enderecos = response.getJSONArray("enderecos");

                            for (int i = 0; i < telefones.length(); i++) {
                                JSONObject telefone = (JSONObject) telefones.get(i);
                                Telefone telefoneObj = new Telefone(
                                        telefone.getInt("id"),
                                        telefone.getInt("contato_id"),
                                        telefone.getString("numero"),
                                        telefone.getString("tipo"));
                                telefoneList.add(telefoneObj);
                            }

                            rvTelefones.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            rvTelefonesAdapter = new RecyclerTelefonesAdapter(getApplicationContext(), telefoneList);
                            rvTelefones.setAdapter(rvTelefonesAdapter);

                            for (int i = 0; i < enderecos.length(); i++) {
                                JSONObject endereco = (JSONObject) enderecos.get(i);
                                Endereco enderecoObj = new Endereco(
                                        endereco.getInt("id"),
                                        endereco.getInt("contato_id"),
                                        endereco.getString("cep"),
                                        endereco.getString("logradouro"),
                                        endereco.getString("bairro"),
                                        endereco.getString("cidade"),
                                        endereco.getString("uf"));
                                enderecoList.add(enderecoObj);
                            }

                            rvEnderecos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            rvEnderecosAdapter = new RecyclerEnderecosAdapter(getApplicationContext(), enderecoList);
                            rvEnderecos.setAdapter(rvEnderecosAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error: ", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + MainActivity.ACCESS_TOKEN);
                return headerMap;
            }

        };
        queue.add(jsonObjectRequest);
    }


    private void deleteTelefoneById(int position, long contato_id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.0.104:8000/api/telefone/";
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                url + telefoneList.get(position).getId(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                telefoneList.remove(position);
                telefoneList.clear();
                enderecoList.clear();
                getContatoDetails(contato_id);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + MainActivity.ACCESS_TOKEN);
                return headerMap;
            }

        };
        queue.add(stringRequest);
    }

    private void deleteEnderecoById(int position, long contato_id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.0.104:8000/api/endereco/";
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                url + enderecoList.get(position).getId(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                enderecoList.remove(position);
                enderecoList.clear();
                telefoneList.clear();
                getContatoDetails(contato_id);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + MainActivity.ACCESS_TOKEN);
                return headerMap;
            }

        };
        queue.add(stringRequest);
    }

}