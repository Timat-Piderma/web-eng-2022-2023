package org.univaq.swa.auleweb.aulewebrest.model;

public class Gruppo {
    private Integer id;
    private String nome;
    private String descrizione;
    private Aula[] aule;
    
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
     * @return the aule
     */
    public Aula[] getAule() {
        return aule;
    }

    /**
     * @param aule the aule to set
     */
    public void setAule(Aula[] aule) {
        this.aule = aule;
    }
}
