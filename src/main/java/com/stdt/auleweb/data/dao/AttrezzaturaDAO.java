package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.data.model.Aula;
import java.util.List;

public interface AttrezzaturaDAO {
    
    Attrezzatura createAttrezzatura() throws DataException;
    
    Attrezzatura getAttrezzatura(int attrezzatura_key) throws DataException;
    
    List<Attrezzatura> getAttrezzature() throws DataException;
    
    List<Attrezzatura> getAttrezzature(Aula aula) throws DataException;
    
    void storeAttrezzatura(Attrezzatura attrezzatura) throws DataException;
}
