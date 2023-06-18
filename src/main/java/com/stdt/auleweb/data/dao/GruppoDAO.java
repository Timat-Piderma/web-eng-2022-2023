package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import java.util.List;

public interface GruppoDAO {

    Gruppo createGruppo() throws DataException;

    Gruppo getGruppo(int gruppo_key) throws DataException;

    List<Gruppo> getGruppi() throws DataException;

    void storeGruppo(Gruppo gruppo) throws DataException;

    void deleteGruppo(Gruppo gruppo) throws DataException;
}
