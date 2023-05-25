package com.stdt.auleweb.data.model;

import java.util.List;

public interface Responsabile {
    
    String getNome();
    
    void setNome(String nome);
    
    String getemailResponsabile();
    
    void setEmailResponsabile(String emailResponsabile);
    
    int getId();
    
    void setId(int id);
    
    List<Evento> getEventi();
    
    void setEventi(List<Evento> eventi);
}
