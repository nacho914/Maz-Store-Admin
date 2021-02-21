package com.example.mazstoreadmin;

import android.app.AlertDialog;
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

public class MainActivity_AddUserRestaurante extends AppCompatActivity {

    Spinner sHoraInicia;
    Spinner sHoraFin;
    EditText nombre;
    EditText domicilio;
    EditText telefono;
    EditText correo;
    EditText contra;


    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Usuarios/UsuariosRestaurantes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__add_user_restaurante);

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
        if(!correo.getText().toString().isEmpty() && !contra.getText().toString().isEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo.getText().toString(), contra.getText().toString()).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    agregarBaseDatos();
                }

                else {

                    mostrarDialogo("Registro",task.getException().toString());
                }

            });
        }
        else
        {
            mostrarDialogo("Registro","Favor de ingresar correo y contraseña");
        }
    }

    public void agregarBaseDatos()
    {

        DatabaseReference NewUserPush = ref.push();
        UsuariosRestaurantes usuario = llenarUsuario();

        NewUserPush.setValue(usuario);

        mostrarDialogo("Registro","Tu registro fue correctamente realizado ");

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

    public  void mostrarDialogo(String sTitulo, String sMensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(sTitulo);
        builder.setMessage(sMensaje);
        //builder.setPositiveButton("OK", null);
        builder.setNeutralButton("Entendido",null);
        builder.setInverseBackgroundForced(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}