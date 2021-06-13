package com.izaram.agendaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.izaram.agendaapp.MainActivity;
import com.izaram.agendaapp.R;
import com.izaram.agendaapp.model.Contato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdicionarContatoActivity extends AppCompatActivity {

    TextInputEditText nome, categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_contato);

        Button btnSave = findViewById(R.id.btn_save);

        if(getIntent().getIntExtra("ACTION", -1) == 0){
            TextView textViewTitle = findViewById(R.id.tv_add_contato_titulo);
            textViewTitle.setText("Editar Contato");
            btnSave.setText("Salvar");

            Contato contato = (Contato) getIntent().getSerializableExtra("CONTATO");

            nome = findViewById(R.id.ti_nome);
            categoria = findViewById(R.id.ti_categoria);

            nome.setText(contato.getNome());
            categoria.setText(contato.getCategoria());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action = getIntent().getIntExtra("ACTION", -1);
                if (action == 1) {
                    adicionarContato(MainActivity.user_id);
                }else if (action == 0) {
                    editarContato();
                }else {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro em sua solicitação!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editarContato() {

        nome = findViewById(R.id.ti_nome);
        categoria = findViewById(R.id.ti_categoria);

        Contato contato = (Contato) getIntent().getSerializableExtra("CONTATO");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nome", nome.getText().toString());
            jsonObject.put("categoria", categoria.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.104:8000/api/update/contatos/" + contato.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ListaContatosActivity.class));
                finish();
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
        queue.add(jsonObjectRequest);
    }

    private void adicionarContato(int user_id) {

        nome = findViewById(R.id.ti_nome);
        categoria = findViewById(R.id.ti_categoria);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nome", nome.getText().toString());
            jsonObject.put("categoria", categoria.getText().toString());
            jsonObject.put("user_id", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.104:8000/api/contatos/" + user_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ListaContatosActivity.class));
                finish();
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
        queue.add(jsonObjectRequest);
    }

}