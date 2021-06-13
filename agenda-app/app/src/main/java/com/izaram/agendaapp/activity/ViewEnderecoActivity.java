package com.izaram.agendaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.izaram.agendaapp.R;
import com.izaram.agendaapp.model.Endereco;

import android.os.Bundle;
import android.widget.TextView;

public class ViewEnderecoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_endereco);

        Endereco endereco = (Endereco) getIntent().getSerializableExtra("ENDERECO");

        TextView cep, uf, cidade, logradouro, bairro;
        cep = findViewById(R.id.tv_view_cep);
        uf = findViewById(R.id.tv_view_uf);
        cidade = findViewById(R.id.tv_view_cidade);
        logradouro = findViewById(R.id.tv_view_logradouro);
        bairro = findViewById(R.id.tv_view_bairro);

        cep.setText("CEP: " + endereco.getCep());
        uf.setText("UF: " + endereco.getUf());
        cidade.setText("Cidade: " + endereco.getCidade());
        logradouro.setText("Logradouro: " + endereco.getLogradouro());
        bairro.setText("Bairro: " + endereco.getBairro());
    }
}