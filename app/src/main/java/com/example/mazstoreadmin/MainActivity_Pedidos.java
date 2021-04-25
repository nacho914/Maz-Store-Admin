package com.example.mazstoreadmin;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mazstoreadmin.ListaPedidos.ListAdapterPedidos;
import com.example.mazstoreadmin.ListaPedidos.list_elementPedidos;
import com.example.mazstoreadmin.modelos.Pedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Pedidos extends AppCompatActivity {

    List<list_elementPedidos> elements;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Pedidos");
    private ProgressDialog progressDialog;
    Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__pedidos);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Verificando Datos");
        progressDialog.show();

        cargarSpinner();
    }

    public void cargarSpinner()
    {
        dropdown = findViewById(R.id.spEstatus);
        String[] items = new String[]{"Activos", "En proceso", "Finalizados"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextSize(20);

                switch (position) {
                    case 0:
                        cargarDatosActivos();
                        break;
                    case 1:
                        cargarDatosRepartidor();
                        break;
                    case 2:
                        cargarDatosFinalizados();
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
    }

    public void cargarDatosActivos()
    {
        ref.child("Activos").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                elements = new ArrayList<>();

                for (DataSnapshot PedidoSnapshot: dataSnapshot.getChildren()) {

                    Pedidos pedido = PedidoSnapshot.getValue(Pedidos.class);

                    assert pedido != null;
                    if(pedido.TrabajadorKey.isEmpty()) {

                        elements.add(new list_elementPedidos(pedido.NombreNegocio, PedidoSnapshot.getKey(), "$ " + pedido.Precio,pedido.RestauranteKey,pedido.getTimestampCreatedLong(),pedido.TiempoPedido,dropdown.getSelectedItemPosition()));
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

    public void cargarDatosRepartidor()
    {
        ref.child("Activos").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                elements = new ArrayList<>();

                for (DataSnapshot PedidoSnapshot: dataSnapshot.getChildren()) {
                    Pedidos pedido = PedidoSnapshot.getValue(Pedidos.class);

                    assert pedido != null;
                    if(!pedido.TrabajadorKey.isEmpty()) {

                        elements.add(new list_elementPedidos(pedido.NombreNegocio, PedidoSnapshot.getKey(), "$ " + pedido.Precio, pedido.RestauranteKey,pedido.getTimestampCreatedLong(),pedido.TiempoPedido,dropdown.getSelectedItemPosition()));
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

    public void cargarDatosFinalizados()
    {
        ref.child("Finalizados").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                elements = new ArrayList<>();

                for (DataSnapshot PedidoSnapshot: dataSnapshot.getChildren()) {
                    Pedidos pedido = PedidoSnapshot.getValue(Pedidos.class);

                    //if(pedido.RestauranteKey.equals(keyRestaurante)) {
                    assert pedido != null;
                    elements.add(new list_elementPedidos(pedido.NombreNegocio, PedidoSnapshot.getKey(), "$ " + pedido.Precio, pedido.RestauranteKey,pedido.getTimestampCreatedLong(),pedido.TiempoPedido, dropdown.getSelectedItemPosition()));
                    //}
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

    public void cargarDatosLista()
    {
        ListAdapterPedidos listAdapter = new ListAdapterPedidos(elements,this);
        RecyclerView recyclerView = findViewById(R.id.ListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    public void regresarPrincipal(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

