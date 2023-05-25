package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.framework.data.DataException;

public interface CorsoDAO {

    CorsoDAO createCorso() throws DataException;

    CorsoDAO getCorso(Evento evento) throws DataException;
}
