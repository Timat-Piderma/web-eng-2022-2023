package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.framework.data.DataException;

public interface PosizioneDAO {

    Posizione createPosizione() throws DataException;
    
    Posizione getPosizione(int posizione_key) throws DataException;

    Posizione getPosizioneByAula(Aula aula) throws DataException;
    
    void storePosizione(Posizione posizione) throws DataException;
}
