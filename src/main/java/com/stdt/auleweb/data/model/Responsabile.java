package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;
import java.util.List;

public interface Responsabile extends DataItem<Integer>{
    
    String getNome();
    
    void setNome(String nome);
    
    String getemailResponsabile();
    
    void setEmailResponsabile(String emailResponsabile);
    
    int getId();
    
    void setId(int id);
    
    List<Evento> getEventi();
    
    void setEventi(List<Evento> eventi);
}
