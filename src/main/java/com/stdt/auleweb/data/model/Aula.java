package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;
import java.util.List;

public interface Aula extends DataItem<Integer>{
    
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
    
    void addAttrezzatura(Attrezzatura attrezzatura);
    
    Gruppo getGruppo();
    
    void setGruppo(Gruppo gruppo);
    
    Posizione getPosizione();
    
    void setPosizione(Posizione posizione);
    
    List<Evento> getEventi();
    
    void setEventi(List<Evento> eventi);
    
    void addEvento(Evento evento);
    
}
