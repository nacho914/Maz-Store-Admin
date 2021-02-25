package com.example.mazstoreadmin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mazstoreadmin.modelos.UsuariosRepartidores;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

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
    Button   BotonCrear;

    private ProgressDialog progressDialog;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Usuarios/UsuariosRepartidores");


    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__add_user_repartidor);

        progressDialog = new ProgressDialog(this);

        fechaLicencia =findViewById(R.id.mFechaLicencia);
        fechaSeguro=findViewById(R.id.mFechaSeguro);
        nombre=findViewById(R.id.mNombre);
        domicilio=findViewById(R.id.mDomicilio);
        telefono=findViewById(R.id.mTelefono);
        teleEmer=findViewById(R.id.mTelefonoEmer);
        correo=findViewById(R.id.mCorreo);
        contra=findViewById(R.id.mContra);
        nombreSeguro=findViewById(R.id.mNombreSeguro);
        BotonCrear=findViewById(R.id.btnCrearUsuario);


        addEscuchadores();

        sTipoSangre = findViewById(R.id.mTipoSangre);
        String[] items = new String[]{"Selecciona tipo de sangre","O-", "O+", "A-","A+","B-","B+","AB-","AB+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sTipoSangre.setAdapter(adapter);

    }

    @SuppressLint("SetTextI18n")
    public void addEscuchadores()
    {
        fechaLicencia.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(MainActivity_AddUserRepartidor.this,android.R.style.Theme_Holo_Dialog,
                    (view, year1, monthOfYear, dayOfMonth) -> fechaLicencia.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            picker.show();
        });

        fechaSeguro.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(MainActivity_AddUserRepartidor.this,android.R.style.Theme_Holo_Dialog,
                    (view, year12, monthOfYear, dayOfMonth) -> fechaSeguro.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year12), year, month, day);
            picker.show();
        });
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

                            mostrarDialogo("Registro", Objects.requireNonNull(task.getException()).toString(), false);
                            progressDialog.dismiss();
                        }

                    });
                } else {
                    mostrarDialogo("Registro", "Favor de ingresar correo y contraseña", false);
                    progressDialog.dismiss();
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
        UsuariosRepartidores usuario = llenarUsuario();

        NewUserPush.setValue(usuario);
        progressDialog.dismiss();
        mostrarDialogo("Registro","Tu registro fue correctamente realizado ",true);

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

        return new UsuariosRepartidores(sNombre, sFechaLic, sDomi, lTele, tipoSangre,lTelEmer,sCorreo,sPassword,sNombreSeguro,sFechaSeg,"");
    }


    public Boolean validarDatos()
    {
        boolean bRegresa=false;

        if(fechaLicencia.getText().toString().isEmpty() || fechaSeguro.getText().toString().isEmpty() || nombre.getText().toString().isEmpty()
                || nombre.getText().toString().isEmpty() || domicilio.getText().toString().isEmpty() || telefono.getText().toString().isEmpty()
                || teleEmer.getText().toString().isEmpty() || correo.getText().toString().isEmpty() || contra.getText().toString().isEmpty()
                ||nombreSeguro.getText().toString().isEmpty() || sTipoSangre.getSelectedItemId()==0 )
        {
            mostrarDialogo("Registro","Favor de validar que haya agregado todos los datos",false);
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





