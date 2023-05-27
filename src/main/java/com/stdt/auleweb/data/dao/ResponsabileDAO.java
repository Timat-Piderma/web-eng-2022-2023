package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.template.model.Responsabile;

public interface ResponsabileDAO {
    
    Responsabile createResponsabile()throws DataException;
    
    Responsabile getResponsabile(int responsabile_key) throws DataException;
    
    Responsabile getResponsabileByEvento(Evento evento)throws DataException;
    
    void storeResponsabile(Responsabile responsabile) throws DataException;
}
