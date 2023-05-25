package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.util.List;

public class AttrezzaturaImpl extends DataItemImpl<Integer> implements Attrezzatura {

    private String nome;
    private List<Aula> aule;

    public AttrezzaturaImpl() {
        super();
        nome = "";
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
