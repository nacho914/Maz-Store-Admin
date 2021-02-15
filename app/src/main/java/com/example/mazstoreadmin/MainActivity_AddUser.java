package com.example.mazstoreadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mazstoreadmin.modelos.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity_AddUser extends AppCompatActivity {

    EditText usuario;
    EditText password;
    EditText nombre;
    Spinner sTipoUsuario;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__add_user);

        usuario =findViewById(R.id.m_username);
        password =findViewById(R.id.m_password);
        nombre=findViewById(R.id.m_nombre);

        sTipoUsuario = findViewById(R.id.spTipoUsuario);
        String[] items = new String[]{"Repartidor", "Restaurante", "Administrador"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sTipoUsuario.setAdapter(adapter);

    }

    public void CreateUser(View view)
    {
        if(!usuario.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        agregarBaseDatos();
                    }

                    else {
                        Toast toast = Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
            });
        }
    }

    public void agregarBaseDatos()
    {

        DatabaseReference NewUserPush = ref.push();
        Usuarios usuario = llenarUsuario();

        NewUserPush.setValue(usuario);

        Toast toast = Toast.makeText(getApplicationContext(),"Tu registro fue correctamente realizado ",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public Usuarios llenarUsuario()
    {

        String sNombre=nombre.getText().toString();
        String sCorreo = usuario.getText().toString();
        String sPassword = password.getText().toString();
        String sTipo = sTipoUsuario.getSelectedItem().toString();
        int iTipo = sTipoUsuario.getSelectedItemPosition();


        Usuarios usuario = new Usuarios(sNombre, sCorreo, sPassword, sTipo, iTipo,"");

        return usuario;
    }
}
