package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.util.List;

public class AulaImpl extends DataItemImpl<Integer> implements Aula {

    private String nome;
    private int capienza;
    private int numeroPreseElettriche;
    private int numeroPreseRete;
    private String emailResponsabile;
    private String note;
    private List<Attrezzatura> attrezzature;
    private Gruppo gruppo;
    private Posizione posizione;
    private List<Evento> eventi;

    public AulaImpl() {
        super();
        nome = "";
        capienza = 0;
        numeroPreseElettriche = 0;
        numeroPreseRete = 0;
        emailResponsabile = "";
        note = "";
        attrezzature = null;
        gruppo = null;
        posizione = null;
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
    public int getCapienza() {
        return capienza;
    }

    @Override
    public void setCapienza(int id) {
        this.capienza = capienza;
    }

    @Override
    public String getEmailResponsabile() {
        return emailResponsabile;
    }

    @Override
    public void setEmailResponsabile(String emailResponsabile) {
        this.emailResponsabile = emailResponsabile;
    }

    @Override
    public int getNumeroPreseElettriche() {
        return numeroPreseElettriche;
    }

    @Override
    public void setNumeroPreseElettriche(int numeroPreseElettriche) {
        this.numeroPreseElettriche = numeroPreseElettriche;
    }

    @Override
    public int getNumeroPreseRete() {
        return numeroPreseRete;
    }

    @Override
    public void setNumeroPreseRete(int numeroPreseRete) {
        this.numeroPreseRete = numeroPreseRete;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public List<Attrezzatura> getAttrezzature() {
        return attrezzature;
    }

    @Override
    public void setAttrezzature(List<Attrezzatura> attrezzature) {
        this.attrezzature = attrezzature;
    }

    @Override
    public void addAttrezzatura(Attrezzatura attrezzatura) {
        this.attrezzature.add(attrezzatura);
    }

    @Override
    public Gruppo getGruppo() {
        return gruppo;
    }

    @Override
    public void setGruppo(Gruppo gruppo) {
        this.gruppo = gruppo;
    }

    @Override
    public Posizione getPosizione() {
        return posizione;
    }

    @Override
    public void setPosizione(Posizione posizione) {
        this.posizione = posizione;
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
