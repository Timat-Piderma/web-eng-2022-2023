package com.stdt.auleweb.data.model;

import java.util.List;

public interface Corso {
    
    String getNome();
    
    void setNome(String nome);
    
    int getId();
    
    void setId(int id);
    
    List<Evento> getEventi();
    
    void setEventi(List<Evento> eventi);
}
