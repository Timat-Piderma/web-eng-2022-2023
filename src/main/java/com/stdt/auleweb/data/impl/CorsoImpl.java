package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.util.List;

public class CorsoImpl extends DataItemImpl<Integer> implements Corso {

    private String nome;
    private List<Evento> eventi;

    public CorsoImpl() {
        super();
        nome = "";
        eventi = null;
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
    public List<Evento> getEventi() {
        return eventi;
    }

    @Override
    public void setEventi(List<Evento> eventi) {
        this.eventi = eventi;
    }

    @Override
    public void addEvento(Evento evento) {
        this.eventi.add(evento);
    }
}
