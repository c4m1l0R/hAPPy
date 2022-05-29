package com.example.happy.modelos;

public class Regalo {

    private String nombre = "";
    private String link = "";
    private String idRegalo = "";

    public Regalo(){ }

    public Regalo(String nombre, String link) {

        this.nombre = nombre;
        this.link = link;
    }

    public Regalo(String nombre, String link, String idRegalo){

        this.nombre = nombre;
        this.link = link;
        this.idRegalo = idRegalo;

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIdRegalo() {
        return idRegalo;
    }

    public void setIdRegalo(String idRegalo) {
        this.idRegalo = idRegalo;
    }
}
