package com.jk.sismos.main.data.model;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class Earthquake {

    @Element(name = "title")
    private String title;
    @Element(name = "estado")
    private String estado;

    @Element(name = "description")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "title='" + title + '\'' +
                ", estado='" + estado + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}