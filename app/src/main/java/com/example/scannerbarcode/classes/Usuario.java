package com.example.scannerbarcode.classes;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int idUsuario;
    private String apelidoUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String telefoneUsuario;
    private int nivelUsuario;
    private String tipoUsuario;

    public Usuario(){
    }

    public Usuario(int idUsuario, String apelidoUsuario, String nomeUsuario, String emailUsuario, String telefoneUsuario, int nivelUsuario, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.apelidoUsuario = apelidoUsuario;
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.telefoneUsuario = telefoneUsuario;
        this.nivelUsuario = nivelUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    public String getTelefoneUsuario() {
        return telefoneUsuario;
    }

    public void setTelefoneUsuario(String telefoneUsuario) {
        this.telefoneUsuario = telefoneUsuario;
    }

    public String getApelidoUsuario() {
        return apelidoUsuario;
    }

    public void setApelidoUsuario(String apelidoUsuario) {
        this.apelidoUsuario = apelidoUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public int getNivelUsuario() {
        return nivelUsuario;
    }

    void setNivelUsuario(int nivelUsuario) {
        this.nivelUsuario = nivelUsuario;
    }
}
