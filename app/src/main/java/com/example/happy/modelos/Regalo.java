package com.example.happy.modelos;

public class Regalo {

    private String nombre = "";
    private String link = "";
    private String idRegalo = "";
    private String regaloReservado= "";

    public Regalo(){ }

    public Regalo(String nombre, String link, String regaloReservado) {

        this.nombre = nombre;
        this.link = link;
        this.regaloReservado = regaloReservado;

    }

    public Regalo(String nombre, String link, String idRegalo, String regaloReservado){

        this.nombre = nombre;
        this.link = link;
        this.idRegalo = idRegalo;
        this.regaloReservado = regaloReservado;

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

    public String getRegaloReservado() {
        return regaloReservado;
    }

    public void setRegaloReservado(String regaloReservado) {
        this.regaloReservado = regaloReservado;
    }
}
