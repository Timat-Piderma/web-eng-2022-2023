package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Posizione;
import java.util.List;

public interface AulaDAO {
    
    Aula createAula();
    
    List<Aula> getAule();
    
    List<Aula> getAule(Posizione posizione);
    
    void setAule(List<Aula> aule);
   
}
