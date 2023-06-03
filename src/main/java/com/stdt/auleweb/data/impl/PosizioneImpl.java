package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.util.List;

public class PosizioneImpl extends DataItemImpl<Integer> implements Posizione {

    private String luogo;
    private String edificio;
    private String piano;
    private List<Aula> aule;

    public PosizioneImpl() {
        super();
        luogo = "";
        edificio = "";
        piano = "";
        aule = null;
    }

    @Override
    public String getLuogo() {
        return luogo;
    }

    @Override
    public void setLuogo(String luogo) {
        this.luogo = luogo;
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

    @Override
    public void addAula(Aula aula) {
        this.aule.add(aula);
    }
}
