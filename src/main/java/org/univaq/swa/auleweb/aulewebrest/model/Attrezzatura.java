package org.univaq.swa.auleweb.aulewebrest.model;

public class Attrezzatura {
    private Integer id;
    private String nome;
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
