package com.conectatangara.models;

import java.util.Date; // Para a data

public class Ocorrencia {
    private String id; // ID do documento no Firestore
    private String titulo;
    private String descricao;
    private String categoria;
    private String status; // Ex: "Recebida", "Em Análise", "Resolvida", etc.
    private Date dataRegistro;
    private double latitude;
    private double longitude;
    // private String userId; // ID do usuário que registrou
    // private ArrayList<String> mediaUrls; // Lista de URLs das mídias no Firebase Storage

    // Construtor vazio é necessário para o Firestore
    public Ocorrencia() {
    }

    // Construtor com alguns campos (pode adicionar mais conforme necessário)
    public Ocorrencia(String id, String titulo, String descricao, String categoria, String status, Date dataRegistro) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.status = status;
        this.dataRegistro = dataRegistro;
    }

    // Getters e Setters (gerados automaticamente ou escritos manualmente)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Adicione getters e setters para userId e mediaUrls se os adicionar
}
    