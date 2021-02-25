package com.example.mazstoreadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mazstoreadmin.modelos.UsuariosRestaurantes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity_AddUserRestaurante extends AppCompatActivity {

    Spinner sHoraInicia;
    Spinner sHoraFin;
    EditText nombre;
    EditText domicilio;
    EditText telefono;
    EditText correo;
    EditText contra;

    private ProgressDialog progressDialog;


    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Usuarios/UsuariosRestaurantes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__add_user_restaurante);

        progressDialog = new ProgressDialog(this);

        sHoraInicia = findViewById(R.id.sHoraInicia);
        sHoraFin = findViewById(R.id.sHoraFin);

        nombre=findViewById(R.id.mNombre);
        domicilio=findViewById(R.id.mDomicilio);
        telefono=findViewById(R.id.mTelefono);
        correo=findViewById(R.id.mCorreo);
        contra=findViewById(R.id.mContra);

        llenarSpinner();

    }

    public void llenarSpinner()
    {
        String[] items= new String[49];
        for(int i=0; i<=48; i++)
        {
            if(i%2==0) {
                items[i]= i / 2 +":00";
            }
            else
            {
                items[i]= i / 2 +":30";
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sHoraInicia.setAdapter(adapter);
        sHoraFin.setAdapter(adapter);
    }

    public void CreateUser(View view)
    {
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Creando usuario");
        progressDialog.show();

        if(isInternetAvailable()) {
            if (validarDatos()) {
                if (!correo.getText().toString().isEmpty() && !contra.getText().toString().isEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo.getText().toString(), contra.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            agregarBaseDatos();
                        } else {
                            progressDialog.dismiss();
                            mostrarDialogo("Registro", Objects.requireNonNull(task.getException()).toString(), false);
                        }

                    });
                } else {
                    progressDialog.dismiss();
                    mostrarDialogo("Registro", "Favor de ingresar correo y contraseña", false);
                }
            } else {
                progressDialog.dismiss();
            }
        }
        else
        {
            progressDialog.dismiss();
            mostrarDialogo("Registro","Lo sentimos no encontramos internet disponible",true);
        }
    }

    public void agregarBaseDatos()
    {

        DatabaseReference NewUserPush = ref.push();
        UsuariosRestaurantes usuario = llenarUsuario();

        NewUserPush.setValue(usuario);

        progressDialog.dismiss();
        mostrarDialogo("Registro","Tu registro fue correctamente realizado ",true);

    }

    public UsuariosRestaurantes llenarUsuario()
    {

        String sNombre=nombre.getText().toString();
        String sDomi =domicilio.getText().toString();
        long lTele = Long.parseLong(telefono.getText().toString());
        String sCorreo = correo.getText().toString();
        String sPassword = contra.getText().toString();
        String sHoraIni = sHoraInicia.getSelectedItem().toString();
        String sHoraFinal = sHoraFin.getSelectedItem().toString();

        return new UsuariosRestaurantes(sNombre, sDomi, lTele, sCorreo, sPassword,sHoraIni,sHoraFinal,"");
    }

    public Boolean validarDatos()
    {
        boolean bRegresa=false;

        if(nombre.getText().toString().isEmpty() || domicilio.getText().toString().isEmpty() || telefono.getText().toString().isEmpty()
            ||correo.getText().toString().isEmpty() || contra.getText().toString().isEmpty() || (sHoraInicia.getSelectedItemId()>sHoraFin.getSelectedItemId()))

        {
            mostrarDialogo("Registro","Favor de validar que los datos sean correctos",false);
        }
        else
        {
            bRegresa=true;
        }

        return bRegresa;
    }


    public  void mostrarDialogo(String sTitulo, String sMensaje,boolean bFinaliza)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(sTitulo);
        builder.setMessage(sMensaje);
        //builder.setPositiveButton("OK", null);
        if(bFinaliza)
        {builder.setNeutralButton("Entendido", (dialog, which) -> {
            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });

        }
        else {
            builder.setNeutralButton("Entendido", null);
        }
        builder.setInverseBackgroundForced(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}