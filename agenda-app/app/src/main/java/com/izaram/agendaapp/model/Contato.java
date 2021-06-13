package com.izaram.agendaapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Contato implements Serializable {

    private Long id;
    private String nome;
    private String categoria;
    private ArrayList<String> telefones;
    private ArrayList<String> enderecos;

    public Contato(Long id, String nome, String categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Contato(Long id, String nome, String categoria, ArrayList<String> telefones, ArrayList<String> enderecos) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.telefones = telefones;
        this.enderecos = enderecos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ArrayList<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(ArrayList<String> telefones) {
        this.telefones = telefones;
    }

    public ArrayList<String> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(ArrayList<String> enderecos) {
        this.enderecos = enderecos;
    }
}
