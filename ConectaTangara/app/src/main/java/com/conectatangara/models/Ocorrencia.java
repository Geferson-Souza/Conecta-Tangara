package com.conectatangara.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp; // Import para @ServerTimestamp

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ocorrencia {
    private String id; // ID do documento no Firestore
    private String userId;
    private String titulo;
    private String descricao;
    private String categoria;
    private String status;
    private GeoPoint localizacao; // Usar GeoPoint para latitude/longitude
    private String enderecoTexto; // Adicionado para consistência com o seeder
    private String bairro;
    @ServerTimestamp // Firestore preencherá automaticamente no servidor na criação
    private Date dataRegistro;
    @ServerTimestamp // Firestore preencherá automaticamente no servidor na criação e atualização
    private Date dataUltimaAtualizacao;
    private List<String> mediaUrls;
    private String atribuidoPara; // UID do funcionário
    private String resolucaoFuncionario;

    // Construtor vazio é necessário para o Firestore
    public Ocorrencia() {
        this.mediaUrls = new ArrayList<>(); // Inicializa a lista para evitar NullPointerException
    }

    // Construtor mais completo (opcional, para testes ou se não usar @ServerTimestamp para datas)
    public Ocorrencia(String userId, String titulo, String descricao, String categoria, String status,
                      double latitude, double longitude, String enderecoTexto, String bairro, List<String> mediaUrls,
                      String atribuidoPara, String resolucaoFuncionario, Date dataRegistro, Date dataUltimaAtualizacao) {
        this.userId = userId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.status = status;
        this.localizacao = new GeoPoint(latitude, longitude);
        this.enderecoTexto = enderecoTexto;
        this.bairro = bairro;
        this.dataRegistro = dataRegistro; // Se não usar @ServerTimestamp, defina aqui
        this.dataUltimaAtualizacao = dataUltimaAtualizacao; // Se não usar @ServerTimestamp, defina aqui
        this.mediaUrls = mediaUrls != null ? new ArrayList<>(mediaUrls) : new ArrayList<>();
        this.atribuidoPara = atribuidoPara;
        this.resolucaoFuncionario = resolucaoFuncionario;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public GeoPoint getLocalizacao() { return localizacao; }
    public void setLocalizacao(GeoPoint localizacao) { this.localizacao = localizacao; }

    public String getEnderecoTexto() { return enderecoTexto; }
    public void setEnderecoTexto(String enderecoTexto) { this.enderecoTexto = enderecoTexto; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public Date getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(Date dataRegistro) { this.dataRegistro = dataRegistro; }

    public Date getDataUltimaAtualizacao() { return dataUltimaAtualizacao; }
    public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) { this.dataUltimaAtualizacao = dataUltimaAtualizacao; }

    public List<String> getMediaUrls() { return mediaUrls; }
    public void setMediaUrls(List<String> mediaUrls) { this.mediaUrls = mediaUrls; }

    public String getAtribuidoPara() { return atribuidoPara; }
    public void setAtribuidoPara(String atribuidoPara) { this.atribuidoPara = atribuidoPara; }

    public String getResolucaoFuncionario() { return resolucaoFuncionario; }
    public void setResolucaoFuncionario(String resolucaoFuncionario) { this.resolucaoFuncionario = resolucaoFuncionario; }
}
