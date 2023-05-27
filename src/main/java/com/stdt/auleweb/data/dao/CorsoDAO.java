package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.framework.data.DataException;
import java.util.List;

public interface CorsoDAO {

    Corso createCorso() throws DataException;
    
    Corso getCorso(int corso_key) throws DataException;

    Corso getCorsoByEvento(Evento evento) throws DataException;

    Corso getCorsoByNome(String nome) throws DataException;

    List<Corso> getCorsi() throws DataException;

    void storeCorso(Corso corso) throws DataException;
}
