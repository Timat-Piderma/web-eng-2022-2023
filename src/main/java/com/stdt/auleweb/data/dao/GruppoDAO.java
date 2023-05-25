package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Gruppo;
import java.util.List;

public interface GruppoDAO {
    
    Gruppo createGruppo();
    
    Gruppo getGruppo(Aula aula);
    
    List<Gruppo> getGruppi();
}
