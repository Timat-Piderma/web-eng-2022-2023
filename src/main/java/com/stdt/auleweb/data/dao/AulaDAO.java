package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.framework.data.DataException;
import java.util.List;

public interface AulaDAO {

    Aula createAula() throws DataException;

    Aula getAula(int aula_key) throws DataException;

    List<Aula> getAule() throws DataException;

    List<Aula> getAuleByAttrezzatura(Attrezzatura attrezzatura) throws DataException;

    List<Aula> getAuleByPosizione(Posizione posizione) throws DataException;

    List<Aula> getAuleByGruppo(Gruppo gruppo) throws DataException;

    List<Aula> getAuleByEvento(Evento evento) throws DataException;

    void storeAula(Aula aula) throws DataException;

    void deleteAula(Aula aula) throws DataException;
}
