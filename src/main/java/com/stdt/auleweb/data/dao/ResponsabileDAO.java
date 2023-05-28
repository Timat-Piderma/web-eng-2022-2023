package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.data.model.Responsabile;
import java.util.List;

public interface ResponsabileDAO {
    
    Responsabile createResponsabile()throws DataException;
    
    Responsabile getResponsabile(int responsabile_key) throws DataException;
    
    Responsabile getResponsabileByEvento(Evento evento)throws DataException;
    
    List<Responsabile> getResponsabili() throws DataException;
    
    void storeResponsabile(Responsabile responsabile) throws DataException;
}
