package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Responsabile;
import com.stdt.auleweb.data.model.Tipologia;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventoImpl extends DataItemImpl<Integer> implements Evento {

    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String descrizione;
    private String nome;
    private Tipologia tipologia;
    private List<Aula> aule;
    private Responsabile responsabile;

    public EventoImpl() {
        super();
        data = null;
        oraFine = null;
        oraInizio = null;
        descrizione = "";
        nome = "";
        tipologia = null;
        aule = null;
        responsabile = null;
    }

    @Override
    public LocalDate getData() {
        return data;
    }

    @Override
    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public LocalTime getOraInizio() {
        return oraInizio;
    }

    @Override
    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    @Override
    public LocalTime getOraFine() {
        return oraFine;
    }

    @Override
    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
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
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public Tipologia getTipologia() {
        return tipologia;
    }

    @Override
    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
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

    @Override
    public Responsabile getResponsabile() {
        return responsabile;
    }

    @Override
    public void setResponsabile(Responsabile responsabile) {
        this.responsabile = responsabile;
    }
}
