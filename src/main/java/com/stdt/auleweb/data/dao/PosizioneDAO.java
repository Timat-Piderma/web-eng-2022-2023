package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.framework.data.DataException;

public interface PosizioneDAO {

    Posizione createPosizione() throws DataException;

    Posizione getPosizione() throws DataException;

    Posizione getPosizione(int id) throws DataException;

    Posizione getPosizione(Aula aula) throws DataException;
}
