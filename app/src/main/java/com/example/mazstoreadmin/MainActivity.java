package com.example.mazstoreadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import com.example.mazstoreadmin.modelos.MainActivity_Login;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isInternetAvailable())
        {
            Intent intent = new Intent(this,MainActivity_Splash.class);
            startActivity(intent);
        }
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

    public void cerrarSesion(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent =new Intent(this, MainActivity_Login.class);
        startActivity(intent);

    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}