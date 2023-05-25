package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.util.List;

public class GruppoImpl extends DataItemImpl<Integer> implements Gruppo {

    private String nome;
    private String descrizione;
    private List<Aula> aule;

    public GruppoImpl() {
        super();
        nome = "";
        descrizione = "";
        aule = null;
    }

    @Override
    public String getNome(String nome) {
        return nome;
    }

    @Override
    public void setNome() {
        this.nome = nome;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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
