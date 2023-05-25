package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import java.util.List;

public interface GruppoDAO {

    Gruppo createGruppo() throws DataException;

    Gruppo getGruppo(Aula aula) throws DataException;

    List<Gruppo> getGruppi() throws DataException;
}
