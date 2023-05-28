package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Responsabile;
import com.stdt.auleweb.data.model.Tipologia;
import com.stdt.auleweb.framework.data.DataItemImpl;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventoImpl extends DataItemImpl<Integer> implements Evento {

    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String descrizione;
    private String nome;
    private Tipologia tipologia;
    private Aula aula;
    private Responsabile responsabile;
    private Corso corso;

    public EventoImpl() {
        super();
        data = null;
        oraFine = null;
        oraInizio = null;
        descrizione = "";
        nome = "";
        tipologia = null;
        aula = null;
        responsabile = null;
        corso = null;
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
    public Aula getAula() {
        return aula;
    }

    @Override
    public void setAula(Aula aul) {
        this.aula = aul;
    }

    @Override
    public Responsabile getResponsabile() {
        return responsabile;
    }

    @Override
    public void setResponsabile(Responsabile responsabile) {
        this.responsabile = responsabile;
    }

    @Override
    public Corso getCorso() {
        return corso;
    }

    @Override
    public void setCorso(Corso corso) {
        this.corso = corso;
    }
}
