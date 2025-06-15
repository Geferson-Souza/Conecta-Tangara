package com.conectatangara.models;

public class Indicator {
    private String id;
    private String name;        // Nome do Indicador
    private String value;       // Valor do Indicador
    private String source;      // Fonte dos dados
    private String period;      // Período a que os dados se referem
    private String bairro;      // NOVO: Bairro a que o indicador se refere (pode ser "Todos")
    private String tema;        // NOVO: Tema do indicador (ex: "Saúde", "Educação")
    private String chartType;   // Tipo de gráfico (ex: "BAR", "LINE", "PIE", "NONE")
    // private Object chartData; // Dados para o gráfico

    // Construtor vazio é necessário para o Firestore
    public Indicator() {
    }

    // Construtor atualizado
    public Indicator(String id, String name, String value, String source, String period, String bairro, String tema) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.source = source;
        this.period = period;
        this.bairro = bairro;
        this.tema = tema;
        this.chartType = "NONE"; // Valor padrão
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    // public Object getChartData() { return chartData; }
    // public void setChartData(Object chartData) { this.chartData = chartData; }
}
