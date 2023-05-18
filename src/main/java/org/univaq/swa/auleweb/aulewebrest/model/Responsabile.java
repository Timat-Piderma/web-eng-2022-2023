package org.univaq.swa.auleweb.aulewebrest.model;

public class Responsabile {
    private String nome;
    private String emailResponsabile;
    private Integer id;
    private Evento[] eventi;

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
     * @return the eventi
     */
    public Evento[] getEventi() {
        return eventi;
    }

    /**
     * @param eventi the eventi to set
     */
    public void setEventi(Evento[] eventi) {
        this.eventi = eventi;
    }
}
