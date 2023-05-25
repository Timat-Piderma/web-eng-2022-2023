package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.framework.data.DataException;
import java.util.List;

public interface AulaDAO {

    Aula createAula() throws DataException;

    List<Aula> getAule() throws DataException;

    List<Aula> getAule(Posizione posizione) throws DataException;

    void setAule(List<Aula> aule) throws DataException;

}
