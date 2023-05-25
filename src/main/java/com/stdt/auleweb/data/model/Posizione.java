package com.stdt.auleweb.data.model;

import com.stdt.auleweb.data.DataItem;
import com.stdt.auleweb.rest.model.Aula;
import java.util.List;

public interface Posizione extends DataItem<Integer> {

    String getNome();
    
    void setNome(String nome);
    
    String getEdificio();
    
    void setEdificio(String edificio);
    
    String getPiano();
    
    void setPiano(String piano);
    
    List<Aula> getAule();
    
    void setAule(List<Aula> aule);

}
