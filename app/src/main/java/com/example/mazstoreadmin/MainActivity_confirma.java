package com.example.mazstoreadmin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mazstoreadmin.modelos.Pedidos;
import com.example.mazstoreadmin.modelos.UsuariosRepartidores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_confirma extends AppCompatActivity {

    //private ProgressDialog progressDialog;
    int iTipoPedido;
    String keyPedido;
    String keyTrabajador;
    String keyRestaurante;
    String keyCorreoRestaurante;
    String nombreRepa;
    Button mConfirma;
    TextView txtTitulo;
    private ProgressDialog progressDialog;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Pedidos");
    DatabaseReference refResta = database.getReference("Usuarios/UsuariosRestaurantes");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_confirma);
        mConfirma=findViewById(R.id.mbtnConfirma);
        txtTitulo=findViewById(R.id.txtTitulo);

        keyCorreoRestaurante="";

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Cargando Datos");
        progressDialog.show();

        Bundle extras = getIntent().getExtras();
        iTipoPedido=extras.getInt("tipoPedido");
        keyPedido = extras.getString("keyPedido");
        keyRestaurante = extras.getString("keyRestaurante");

        if(iTipoPedido ==0) {
            keyTrabajador = extras.getString("keyTrabajador");
            nombreRepa = extras.getString("nombreRepa");
            CargaDatos();
        }
        if(iTipoPedido==1)
        {
            txtTitulo.setText("Confirmar Finalización");
            mConfirma.setText("Finalizar");
            progressDialog.dismiss();
        }




    }

    public void apartarPedido(View view)
    {
        if(iTipoPedido == 0) {
            ref.child("Activos").child(keyPedido).child("TrabajadorKey").setValue(keyTrabajador);
            try {
                enviarNotificaciones();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mostrarDialogo("Apartado", "Haz apartado este pedido", false);
        }
        if(iTipoPedido==1)
        {
            ref.child("Activos").child(keyPedido).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    DatabaseReference NewUserPush = ref.child("Finalizados").push();
                    Pedidos pedidos = snapshot.getValue(Pedidos.class);
                    NewUserPush.setValue(pedidos);

                    snapshot.getRef().removeValue();
                    mostrarDialogo("Finalizado","Haz marcado este pedido como concluido",true);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                }});
        }
    }

    public void CargaDatos()
    {
        refResta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    UsuariosRepartidores usuariosRepartidores = dataSnapshot.child(keyRestaurante).getValue(UsuariosRepartidores.class);
                    keyCorreoRestaurante = usuariosRepartidores.keyNotificaciones;
                    verificaPedido();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void verificaPedido()
    {
        ref.child("Activos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Pedidos pedidos = dataSnapshot.child(keyPedido).getValue(Pedidos.class);

                    mConfirma.setEnabled(pedidos.TrabajadorKey.isEmpty());


                    progressDialog.dismiss();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    public  void mostrarDialogo(String sTitulo, String sMensaje,Boolean bFinaliza)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(sTitulo);
        builder.setMessage(sMensaje);

        builder.setNeutralButton("Entendido", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        builder.setInverseBackgroundForced(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void enviarNotificaciones() throws JSONException
    {
        String postUrl = "https://fcm.googleapis.com/fcm/send";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject obj = new JSONObject();
        JSONObject objChild = new JSONObject();


        obj.put("to", keyCorreoRestaurante);

        obj.put("direct_book_ok",true);

        objChild.put("body","El repartidor "+nombreRepa+" va en camino");
        objChild.put("title","Su pedido fue asignado");
        obj.put("notification",objChild);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, obj, System.out::println, Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "key=AAAAyUXOSJI:APA91bHRcWTrD3LB50qTECUJsKB5pCaUL5pOZBzsMcQHEwX_xyEujXHVkKcB9DpoHM39x_6IWVAUDM3jJ8peL_6W7DmtOJArJUWGmnOtW6RKz9Q7Vaqb2SXiUC5ygyex9OTqUD3sZ7Bc");
                params.put("Content-Type", "application/json");

                return params;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}