package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Evento;
import java.util.List;

public interface EventoDAO {

    Evento createEvento() throws DataException;

    List<Evento> getEventi() throws DataException;

    List<Evento> getEventi(Aula aula) throws DataException;
}
