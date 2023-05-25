package com.stdt.auleweb.data.model;

import java.util.List;

public interface Gruppo {
    
    int getId();
        
    void setId(int id);
    
    String getNome(String nome);
    
    void setNome();
    
    String getDescrizione();
    
    void setDescrizione(String descrizione);
    
    List<Aula> getAule();
    
    void setAule(List<Aula> aule);
}
