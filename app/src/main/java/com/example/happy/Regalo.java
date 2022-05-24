package com.example.happy;

public class Regalo {

    private String nombre = "";
    private String link = "";

    public Regalo(){ }

    public Regalo(String nombre, String link) {
        this.nombre = nombre;
        this.link = link;
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
}
