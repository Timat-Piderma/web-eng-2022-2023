package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Responsabile;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.util.List;

public class ResponsabileImpl extends DataItemImpl<Integer> implements Responsabile {

    private String nome;
    private String email;
    private List<Evento> eventi;

    public ResponsabileImpl() {
        super();
        nome = "";
        email = "";
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
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
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
