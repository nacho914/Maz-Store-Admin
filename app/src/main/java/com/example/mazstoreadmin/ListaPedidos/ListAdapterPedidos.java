package com.example.mazstoreadmin.ListaPedidos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mazstoreadmin.MainActivity_confirma;
import com.example.mazstoreadmin.MainActivity_repartidores;
import com.example.mazstoreadmin.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListAdapterPedidos  extends RecyclerView.Adapter<ListAdapterPedidos.ViewHolder>{
    private List<list_elementPedidos> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapterPedidos(List<list_elementPedidos> mData, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    @Override
    public int getItemCount()
    { return mData.size();}

    @Override
    public ListAdapterPedidos.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
    {
        View view = mInflater.inflate(R.layout.pedidos_element,null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull final ListAdapterPedidos.ViewHolder holder, final int position)
    {
        holder.binData(mData.get(position));
        if(holder.iTipoPedido != 2)
        {
            Date currentTime = Calendar.getInstance().getTime();

            long lTimeMore = ((holder.iTiempoPedido*60)*1000);
            if (holder.timer != null) {
                holder.timer.cancel();
            }
            long timer =(holder.lHoraPedido+lTimeMore)-currentTime.getTime();

            //timer = timer*1000;

            if(timer>0) {
                holder.timer = new CountDownTimer(timer, 1000) {
                    public void onTick(long millisUntilFinished) {

                        int iSegundos= (int) (millisUntilFinished/1000);
                        int hours = iSegundos / 3600;
                        int minutes = (iSegundos % 3600) / 60;
                        int seconds = iSegundos % 60;

                        holder.hora.setText("Listo en: \n"+String.format("%02d:%02d:%02d", hours, minutes, seconds));
                    }

                    public void onFinish() {
                        holder.hora.setText("AHORA");
                    }
                }.start();
            }
            else
            {
                holder.hora.setText("AHORA");
            }
        }
        else
        {
            holder.hora.setText("Finalizado");
        }
    }

    public void setItems(List<list_elementPedidos> items)
    {mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView titulo;
        TextView dinero;
        TextView hora;
        int iTiempoPedido;
        long lHoraPedido;
        int iTipoPedido;
        CountDownTimer timer;

        ViewHolder(View itemView)
        {
            super(itemView);
            titulo= itemView.findViewById(R.id.m_nombreRepa);
            dinero= itemView.findViewById(R.id.m_telefonoRepa);
            hora= itemView.findViewById(R.id.m_hora);

        }

        void binData(@org.jetbrains.annotations.NotNull final list_elementPedidos item)
        {
            titulo.setText(item.getTitulo());
            dinero.setText(item.getDinero());
            hora.setText("");
            iTiempoPedido=item.tiempoPedido;
            lHoraPedido=item.TiempoActualPedido;
            iTipoPedido=item.tipoPedido;

            itemView.setOnClickListener(v -> {
                if(item.tipoPedido == 0) {
                    Intent intent = new Intent(context, MainActivity_repartidores.class);
                    Bundle extras = new Bundle();
                    extras.putString("keyPedido", item.PedidoKey);
                    extras.putString("keyRestaurante", item.RestauranteKey);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
                else if(item.tipoPedido==1)
                {
                    Intent intent = new Intent(context, MainActivity_confirma.class);
                    Bundle extras = new Bundle();
                    extras.putString("keyPedido", item.PedidoKey);
                    extras.putString("keyRestaurante", item.RestauranteKey);
                    extras.putInt("tipoPedido",item.tipoPedido);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
        }

    }
}
