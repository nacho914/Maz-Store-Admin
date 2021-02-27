package com.example.mazstoreadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mazstoreadmin.modelos.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity_AddUser extends AppCompatActivity {

    EditText usuario;
    EditText password;
    EditText nombre;

    private ProgressDialog progressDialog;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Usuarios/UsuariosAdministradores");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__add_user);

        progressDialog = new ProgressDialog(this);

        usuario =findViewById(R.id.m_username);
        password =findViewById(R.id.m_password);
        nombre=findViewById(R.id.m_nombre);


    }

    public void CreateUser(View view)
    {
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Creando usuario");
        progressDialog.show();

        if(isInternetAvailable())
        {
            if(validarDatos()) {

                if (!usuario.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            agregarBaseDatos();
                        }

                        else {
                            progressDialog.dismiss();
                            mostrarDialogo("Registro",Objects.requireNonNull(task.getException()).toString(),false);

                        }

                    });
                }
            }
            else
            {
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
        Usuarios usuario = llenarUsuario();

        NewUserPush.setValue(usuario);

        progressDialog.dismiss();
        mostrarDialogo("Registro","Tu registro fue correctamente realizado",true);

    }

    public Usuarios llenarUsuario()
    {

        String sNombre=nombre.getText().toString();
        String sCorreo = usuario.getText().toString();
        String sPassword = password.getText().toString();
        //String sTipo = sTipoUsuario.getSelectedItem().toString();
        //int iTipo = sTipoUsuario.getSelectedItemPosition();

        return new Usuarios(sNombre, sCorreo, sPassword,"");
    }

    public Boolean validarDatos()
    {
        boolean bRegresa=false;

        if(nombre.getText().toString().isEmpty() || usuario.getText().toString().isEmpty() || password.getText().toString().isEmpty())
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
