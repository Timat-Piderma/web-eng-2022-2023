package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;
import java.util.List;

public interface Gruppo extends DataItem<Integer>{
    
    String getNome();
    
    void setNome(String nome);
    
    String getDescrizione();
    
    void setDescrizione(String descrizione);
    
    List<Aula> getAule();
    
    void setAule(List<Aula> aule);
    
    void addAula(Aula aula);
}
