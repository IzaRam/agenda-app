package com.izaram.agendaapp.model;

import java.io.Serializable;

public class Telefone implements Serializable {

    private int id;
    private int contato_id;
    private String numero;
    private String tipo;

    public Telefone() { }

    public Telefone(int id, String numero, String tipo) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
    }

    public Telefone(int id, int contato_id, String numero, String tipo) {
        this.id = id;
        this.contato_id = contato_id;
        this.numero = numero;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContato_id() {
        return contato_id;
    }

    public void setContato_id(int contato_id) {
        this.contato_id = contato_id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
