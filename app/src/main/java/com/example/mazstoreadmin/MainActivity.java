package com.example.mazstoreadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void EnviarRegistro(View view)
    {
        Intent intent = new Intent(this,MainActivity_AddUser.class);
        startActivity(intent);
    }

    public void enviarRepartidor(View view)
    {
        Intent intent =new Intent(this,MainActivity_AddUserRepartidor.class);
        startActivity(intent);
    }

    public void enviarRegistroRest(View view)
    {
        Intent intent =new Intent(this,MainActivity_AddUserRestaurante.class);
        startActivity(intent);
    }
}