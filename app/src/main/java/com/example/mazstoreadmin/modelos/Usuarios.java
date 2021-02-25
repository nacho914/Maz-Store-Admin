package com.example.mazstoreadmin.modelos;

import java.util.HashMap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class Usuarios {

    public String Nombre;
    public String Correo;
    public String Password;
    public  String keyNotificaciones;
    HashMap<String, Object> timestampCreated;

    public Usuarios()
    {

    }

    public Usuarios(String sNombre, String sCorreo, String sPassword,String sKey)
    {
        Nombre=sNombre;
        Correo=sCorreo;
        Password=sPassword;
        keyNotificaciones=sKey;

        HashMap<String, Object> timestampNow = new HashMap<>();
        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
        this.timestampCreated = timestampNow;
    }

    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }

    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("timestamp");
    }
}
