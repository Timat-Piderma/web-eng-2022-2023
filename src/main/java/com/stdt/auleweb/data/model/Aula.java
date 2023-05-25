package com.stdt.auleweb.data.model;

import java.util.List;

public interface Aula {
    
    String getNome();
    
    void setNome(String nome);
    
    int getCapienza();
    
    void setCapienza(int id);
    
    String getEmailResponsabile();
    
    void setEmailResponsabile(String emailResponsabile);
    
    int getNumeroPreseElettriche();
    
    void setNumeroPreseElettriche(int numeroPreseElettriche);
    
    int getNumeroPreseRete();
    
    void setNumeroPreseRete(int numeroPreseRete);
    
    String getNote();
    
    void setNote(String note);
    
    List<Attrezzatura> getAttrezzature();
    
    void setAttrezzature(List<Attrezzatura> attrezzature);
    
    Gruppo getGruppo();
    
    void setGruppo(Gruppo gruppo);
    
    Posizione getPosizione();
    
    void setPosizione(Posizione posizione);
    
    List<Evento> getEventi();
    
    void setEventi(List<Evento> eventi);
    
}
