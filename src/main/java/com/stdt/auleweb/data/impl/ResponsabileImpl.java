package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Responsabile;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.util.List;

public class ResponsabileImpl extends DataItemImpl<Integer> implements Responsabile {

    private String nome;
    private String emailResponsabile;
    private List<Evento> eventi;

    public ResponsabileImpl() {
        super();
        nome = "";
        emailResponsabile = "";
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
    public String getemailResponsabile() {
        return emailResponsabile;
    }

    @Override
    public void setEmailResponsabile(String emailResponsabile) {
        this.emailResponsabile = emailResponsabile;
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
