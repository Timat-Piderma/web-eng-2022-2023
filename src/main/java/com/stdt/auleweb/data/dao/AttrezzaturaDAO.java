package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.DataException;
import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.data.model.Aula;
import java.util.List;

public interface AttrezzaturaDAO {
    
    Attrezzatura createAttrezzatura() throws DataException;
    
    List<Attrezzatura> getAttrezzature() throws DataException;
    
    List<Attrezzatura> getAttrezzature(Aula aula) throws DataException;
}
