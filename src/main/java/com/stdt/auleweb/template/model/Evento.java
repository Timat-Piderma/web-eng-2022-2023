package com.stdt.auleweb.template.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Evento {
    
    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String descrizione;
    private String nome;
    private Integer id;
    private Tipologia tipo;
    private List<Aula> aule;
    private Responsabile responsabile;

    /**
     * @return the data
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(LocalDate data) {
        this.data = data;
    }

    /**
     * @return the oraInizio
     */
    public LocalTime getOraInizio() {
        return oraInizio;
    }

    /**
     * @param oraInizio the oraInizio to set
     */
    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    /**
     * @return the oraFine
     */
    public LocalTime getOraFine() {
        return oraFine;
    }

    /**
     * @param oraFine the oraFine to set
     */
    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the tipo
     */
    public Tipologia getTipologia() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipologia(Tipologia tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the aula
     */
    public List<Aula> getAule() {
        return aule;
    }

    /**
     * @param aule the aula to set
     */
    public void setAule(List<Aula> aule) {
        this.aule = aule;
    }

    /**
     * @return the responsabile
     */
    public Responsabile getResponsabile() {
        return responsabile;
    }

    /**
     * @param responsabile the responsabile to set
     */
    public void setResponsabile(Responsabile responsabile) {
        this.responsabile = responsabile;
    }
}
