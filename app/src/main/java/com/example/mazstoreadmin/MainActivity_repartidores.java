package com.example.mazstoreadmin;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mazstoreadmin.ListaRepartidores.ListAdapterRepartidores;
import com.example.mazstoreadmin.ListaRepartidores.list_ElementRepartidores;
import com.example.mazstoreadmin.modelos.UsuariosRepartidores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_repartidores extends AppCompatActivity {

    String keyPedido;
    String keyRestaurante;
    List<list_ElementRepartidores> elements;
    EditText buscador;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Usuarios");
    private ProgressDialog progressDialog;
    String busca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_repartidores);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        buscador=findViewById(R.id.txtBusNombre);
        busca="";

        Bundle extras = getIntent().getExtras();
        keyPedido = extras.getString("keyPedido");
        keyRestaurante =extras.getString("keyRestaurante");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Verificando Datos");
        progressDialog.show();

        cargarDatosActivos();
        cargarListenerBuscador();
    }


    public void cargarDatosActivos()
    {
        ref.child("UsuariosRepartidores").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                elements = new ArrayList<>();

                for (DataSnapshot RepartidorSnapshot: dataSnapshot.getChildren()) {

                    UsuariosRepartidores usuariosRepartidores = RepartidorSnapshot.getValue(UsuariosRepartidores.class);

                    if(usuariosRepartidores.Nombre.contains(busca)) {
                        elements.add(new list_ElementRepartidores(usuariosRepartidores.Nombre, String.valueOf(usuariosRepartidores.Telefono), keyPedido, RepartidorSnapshot.getKey(), keyRestaurante));
                    }
                }
                cargarDatosLista();
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public void cargarListenerBuscador()
    {
        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                busca =s.toString();
                List<list_ElementRepartidores> elements2 = new ArrayList<>();

                for (list_ElementRepartidores repa:elements)
                {
                    if(repa.Nombre.toLowerCase().contains(busca.toLowerCase())) {
                        elements2.add(repa);
                    }
                }
                cargarDatosLista(elements2);
            }
        });
    }

    public void cargarDatosLista()
    {
        ListAdapterRepartidores listAdapter = new ListAdapterRepartidores(elements,this);
        RecyclerView recyclerView = findViewById(R.id.ListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }


    public void cargarDatosLista(List<list_ElementRepartidores> elementsCar)
    {
        ListAdapterRepartidores listAdapter = new ListAdapterRepartidores(elementsCar,this);
        RecyclerView recyclerView = findViewById(R.id.ListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
}

