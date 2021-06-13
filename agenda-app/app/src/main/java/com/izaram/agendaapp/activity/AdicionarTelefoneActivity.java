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
import com.izaram.agendaapp.model.Telefone;

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

public class AdicionarTelefoneActivity extends AppCompatActivity {

    TextInputEditText numero, tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_telefone);

        Button btnSave = findViewById(R.id.btn_save_telefone);
        Contato contato = (Contato) getIntent().getSerializableExtra("CONTATO");

        numero = findViewById(R.id.ti_numero);
        tipo = findViewById(R.id.ti_tipo);

        if(getIntent().getIntExtra("ACTION", -1) == 0){
            TextView textViewTitle = findViewById(R.id.tv_add_telefone_titulo);
            textViewTitle.setText("Editar Telefone");
            btnSave.setText("Salvar");

            Telefone telefone = (Telefone) getIntent().getSerializableExtra("TELEFONE");

            numero.setText(telefone.getNumero());
            tipo.setText(telefone.getTipo());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action = getIntent().getIntExtra("ACTION", -1);
                if (action == 1) {
                    adicionarTelefone(contato);
                }else if (action == 0) {
                    editarTelefone(contato);
                }else {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro em sua solicitação!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void editarTelefone(Contato contato) {

        Telefone telefone = (Telefone) getIntent().getSerializableExtra("TELEFONE");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("numero", numero.getText().toString());
            jsonObject.put("tipo", tipo.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.104:8000/api/telefone/" + telefone.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ViewContatoActivity.class);
                intent.putExtra("CONTATO", contato);
                startActivity(intent);
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


    private void adicionarTelefone(Contato contato) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("numero", numero.getText().toString());
            jsonObject.put("tipo", tipo.getText().toString());
            jsonObject.put("contato_id", contato.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.104:8000/api/telefone";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ViewContatoActivity.class);
                intent.putExtra("CONTATO", contato);
                startActivity(intent);
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