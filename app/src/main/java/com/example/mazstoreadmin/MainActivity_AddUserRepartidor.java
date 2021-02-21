package com.example.mazstoreadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.RemoteInput;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mazstoreadmin.modelos.Usuarios;
import com.example.mazstoreadmin.modelos.UsuariosRepartidores;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity_AddUserRepartidor extends AppCompatActivity {

    EditText fechaLicencia;
    EditText fechaSeguro;
    Spinner sTipoSangre;
    EditText nombre;
    EditText domicilio;
    EditText telefono;
    EditText teleEmer;
    EditText correo;
    EditText contra;
    EditText nombreSeguro;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Usuarios/UsuariosRepartidores");


    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__add_user_repartidor);

        fechaLicencia =findViewById(R.id.mFechaLicencia);
        fechaSeguro=findViewById(R.id.mFechaSeguro);
        nombre=findViewById(R.id.mNombre);
        domicilio=findViewById(R.id.mDomicilio);
        telefono=findViewById(R.id.mTelefono);
        teleEmer=findViewById(R.id.mTelefonoEmer);
        correo=findViewById(R.id.mCorreo);
        contra=findViewById(R.id.mContra);
        nombreSeguro=findViewById(R.id.mNombreSeguro);


        addEscuchadores();

        sTipoSangre = findViewById(R.id.mTipoSangre);
        String[] items = new String[]{"Selecciona tipo de sangre","O-", "O+", "A-","A+","B-","B+","AB-","AB+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sTipoSangre.setAdapter(adapter);

    }

    public void addEscuchadores()
    {
        fechaLicencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity_AddUserRepartidor.this,android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fechaLicencia.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        fechaSeguro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity_AddUserRepartidor.this,android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fechaSeguro.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    public void CreateUser(View view)
    {
        if(!correo.getText().toString().isEmpty() && !contra.getText().toString().isEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo.getText().toString(), contra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        agregarBaseDatos();
                    }

                    else {

                        mostrarDialogo("Registro",task.getException().toString());
                    }

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
        UsuariosRepartidores usuario = llenarUsuario();

        NewUserPush.setValue(usuario);

        mostrarDialogo("Registro","Tu registro fue correctamente realizado ");

    }

    public UsuariosRepartidores llenarUsuario()
    {

        String sNombre=nombre.getText().toString();
        String sDomi =domicilio.getText().toString();
        long lTele = Long.parseLong(telefono.getText().toString());
        long lTelEmer=Long.parseLong(teleEmer.getText().toString());
        String sCorreo = correo.getText().toString();
        String sPassword = contra.getText().toString();
        String tipoSangre = sTipoSangre.getSelectedItem().toString();
        String sNombreSeguro =nombreSeguro.getText().toString();
        String sFechaLic = fechaLicencia.getText().toString();
        String sFechaSeg = fechaSeguro.getText().toString();

        UsuariosRepartidores usuario = new UsuariosRepartidores(sNombre, sFechaLic, sDomi, lTele, tipoSangre,lTelEmer,sCorreo,sPassword,sNombreSeguro,sFechaSeg,"");

        return usuario;
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





