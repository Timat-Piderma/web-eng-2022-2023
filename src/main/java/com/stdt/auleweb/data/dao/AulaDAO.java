package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.framework.data.DataException;
import java.util.List;

public interface AulaDAO {

    Aula createAula() throws DataException;
    
    Aula getAula(int aula_key) throws DataException;
    
    Aula getAulaByNome(String nome) throws DataException;
    
    Aula getAulaById(int id) throws DataException;

    List<Aula> getAule() throws DataException;

    List<Aula> getAuleByPosizione(Posizione posizione) throws DataException;
    
    List<Aula> getAuleByGruppo(Gruppo gruppo) throws DataException;
    
    void storeAula(Aula aula) throws DataException;
    
}
