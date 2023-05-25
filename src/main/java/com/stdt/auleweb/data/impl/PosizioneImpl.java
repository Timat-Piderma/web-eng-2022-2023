package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.framework.data.DataItemImpl;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.template.model.Aula;
import java.util.List;

public class PosizioneImpl extends DataItemImpl<Integer> implements Posizione {

    private String nome;
    private String edificio;
    private String piano;
    private List<Aula> aule;

    public PosizioneImpl() {
        super();
        nome = "";
        edificio = "";
        piano = "";
        aule = null;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getEdificio() {
        return edificio;
    }

    @Override
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    @Override
    public String getPiano() {
        return piano;
    }

    @Override
    public void setPiano(String piano) {
        this.piano = piano;
    }

    @Override
    public List<Aula> getAule() {
        return aule;
    }

    @Override
    public void setAule(List<Aula> aule) {
        this.aule = aule;
    }

}
