package com.stdt.auleweb.data.model;

import java.util.List;

public interface Attrezzatura {
    
    int getId();
    
    void setId(int id);
    
    String getNome();
    
    void setNome(String nome);
    
    List<Aula> getAule();
    
    void setAule(List<Aula> aule);
}
