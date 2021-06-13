package com.izaram.agendaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.izaram.agendaapp.model.Endereco;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdicionarEnderecoActivity extends AppCompatActivity {

    TextInputEditText cep, uf, cidade, logradouro, bairro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_endereco);

        Button btnSave = findViewById(R.id.btn_save_endereco);
        Contato contato = (Contato) getIntent().getSerializableExtra("CONTATO");

        cep = findViewById(R.id.ti_cep);
        uf = findViewById(R.id.ti_uf);
        cidade = findViewById(R.id.ti_cidade);
        logradouro = findViewById(R.id.ti_logradouro);
        bairro = findViewById(R.id.ti_bairro);

        if (getIntent().getIntExtra("ACTION", -1) == 0) {
            TextView textViewTitle = findViewById(R.id.tv_add_endereco_titulo);
            textViewTitle.setText("Editar Endereço");
            btnSave.setText("Salvar");

            Endereco endereco = (Endereco) getIntent().getSerializableExtra("ENDERECO");

            cep.setText(endereco.getCep());
            uf.setText(endereco.getUf());
            cidade.setText(endereco.getCidade());
            logradouro.setText(endereco.getLogradouro());
            bairro.setText(endereco.getBairro());
        }

        else if (getIntent().getIntExtra("ACTION", -1) == 1) {
            // Preencher endereço (Buscar CEP)
            cep.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String cepInput = cep.getText().toString();
                        buscarCEP(cepInput);
                    }
                    return true;
                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action = getIntent().getIntExtra("ACTION", -1);
                if (action == 1) {
                    adicionarEndereco(contato);
                } else if (action == 0) {
                    editarEndereco(contato);
                } else {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro em sua solicitação!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void editarEndereco(Contato contato) {

        Endereco endereco = (Endereco) getIntent().getSerializableExtra("ENDERECO");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cep", cep.getText().toString());
            jsonObject.put("uf", uf.getText().toString());
            jsonObject.put("cidade", cidade.getText().toString());
            jsonObject.put("logradouro", logradouro.getText().toString());
            jsonObject.put("bairro", bairro.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.104:8000/api/endereco/" + endereco.getId();

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


    private void adicionarEndereco(Contato contato) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cep", cep.getText().toString());
            jsonObject.put("uf", uf.getText().toString());
            jsonObject.put("cidade", cidade.getText().toString());
            jsonObject.put("logradouro", logradouro.getText().toString());
            jsonObject.put("bairro", bairro.getText().toString());
            jsonObject.put("contato_id", contato.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.104:8000/api/endereco";

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

    private void buscarCEP(String cepInput) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://viacep.com.br/ws/" +cepInput + "/json/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    cep.setText(response.getString("cep"));
                    uf.setText(response.getString("uf"));
                    cidade.setText(response.getString("localidade"));
                    logradouro.setText(response.getString("logradouro"));
                    bairro.setText(response.getString("bairro"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }


}