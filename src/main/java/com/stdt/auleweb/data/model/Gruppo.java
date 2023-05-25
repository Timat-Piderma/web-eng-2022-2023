package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;
import java.util.List;

public interface Gruppo extends DataItem<Integer>{
    
    int getId();
        
    void setId(int id);
    
    String getNome(String nome);
    
    void setNome();
    
    String getDescrizione();
    
    void setDescrizione(String descrizione);
    
    List<Aula> getAule();
    
    void setAule(List<Aula> aule);
}
