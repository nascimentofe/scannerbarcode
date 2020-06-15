package com.viaexpressa.scannerbarcode.classes;

public class Nota {
    private String cnpj;
    private String dataHora;
    private String numNota;
    private int caminhoImagemNota;

    public Nota(String cnpj, String dataHora, String numNota, int caminhoImagemNota) {
        this.cnpj = cnpj;
        this.dataHora = dataHora;
        this.numNota = numNota;
        this.caminhoImagemNota = caminhoImagemNota;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getNumNota() {
        return numNota;
    }

    public void setNumNota(String numNota) {
        this.numNota = numNota;
    }

    public int getCaminhoImagemNota() {
        return caminhoImagemNota;
    }

    public void setCaminhoImagemNota(int caminhoImagemNota) {
        this.caminhoImagemNota = caminhoImagemNota;
    }
}
