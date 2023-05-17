package org.univaq.swa.auleweb.aulewebrest.model;

public class Aula {

    private String nome;
    private int capienza;
    private String emailResponsabile;
    private int numeroPreseElettriche;
    private int numeroPreseRete;
    private String note;
    //private Attrezzatura[] attrezzatura;
    //private Gruppo gruppo;
    //private Posizione posizione;
    //private Evento[] eventi;

    public Aula() {
        //dati di default
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
     * @return the capienza
     */
    public int getCapienza() {
        return capienza;
    }

    /**
     * @param capienza the capienza to set
     */
    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    /**
     * @return the emailResponsabile
     */
    public String getEmailResponsabile() {
        return emailResponsabile;
    }

    /**
     * @param emailResponsabile the emailResponsabile to set
     */
    public void setEmailResponsabile(String emailResponsabile) {
        this.emailResponsabile = emailResponsabile;
    }

    /**
     * @return the numeroPreseElettriche
     */
    public int getNumeroPreseElettriche() {
        return numeroPreseElettriche;
    }

    /**
     * @param numeroPreseElettriche the numeroPreseElettriche to set
     */
    public void setNumeroPreseElettriche(int numeroPreseElettriche) {
        this.numeroPreseElettriche = numeroPreseElettriche;
    }

    /**
     * @return the numeroPreseRete
     */
    public int getNumeroPreseRete() {
        return numeroPreseRete;
    }

    /**
     * @param numeroPreseRete the numeroPreseRete to set
     */
    public void setNumeroPreseRete(int numeroPreseRete) {
        this.numeroPreseRete = numeroPreseRete;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
}
