package com.izaram.agendaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.izaram.agendaapp.activity.ListaContatosActivity;
import com.izaram.agendaapp.activity.LoginActivity;

public class MainActivity extends AppCompatActivity {

    public static String ACCESS_TOKEN;
    public static int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}