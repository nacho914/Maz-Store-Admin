package com.example.mazstoreadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mazstoreadmin.modelos.MainActivity_Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity_Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    //final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__splash);

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();


    }

    @Override
    public void onStart() {
        super.onStart();

        revisarSesion();

    }

    public void revisarSesion()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            esperar(1);
        }
        else
        {
            esperar(0);
        }

    }

    public void esperar(int iCamino)
    {

        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(() -> {
            if(isInternetAvailable()) {
                Intent intent;
                if (iCamino == 1) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity_Login.class);
                }
                startActivity(intent);
            }
            else {
                mostrarDialogo("Maz Store","Lo sentimos no encontramos internet disponible");
            }

        }, SPLASH_DISPLAY_LENGTH);
    }

    public  void mostrarDialogo(String sTitulo, String sMensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(sTitulo);
        builder.setMessage(sMensaje);
        //builder.setPositiveButton("OK", null);

        builder.setNeutralButton("Intentar de nuevo", (dialog, which) -> revisarSesion());



        builder.setInverseBackgroundForced(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}