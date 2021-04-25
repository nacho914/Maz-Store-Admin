package com.example.mazstoreadmin.ListaRepartidores;

public class list_ElementRepartidores {

    public String Nombre;
    public String Telefono;
    public String PedidoKey;
    public String RepartidorKey;
    public String RestauranteKey;


    public list_ElementRepartidores(String nombre, String telefono, String pedidoKey,String repartidorKey,String restauranteKey) {
        Nombre = nombre;
        Telefono = telefono;
        PedidoKey = pedidoKey;
        RepartidorKey=repartidorKey;
        RestauranteKey=restauranteKey;

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        telefono = Telefono;
    }

    public String getPedidoKey() {
        return PedidoKey;
    }

    public void setPedidoKey(String pedidoKey) {
        PedidoKey = pedidoKey;
    }

    public String getRepartidorKey() {
        return RepartidorKey;
    }

    public void setRepartidorKey(String repartidorKey) {
        RepartidorKey = repartidorKey;
    }

    public String getRestauranteKey() {
        return RestauranteKey;
    }

    public void setRestauranteKey(String restauranteKey) {
        RestauranteKey = restauranteKey;
    }

}
