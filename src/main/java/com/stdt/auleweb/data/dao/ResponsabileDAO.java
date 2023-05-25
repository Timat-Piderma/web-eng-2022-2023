package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.rest.model.Responsabile;

public interface ResponsabileDAO {
    
    Responsabile createResponsabile();
    
    Responsabile getResponsabile(Evento evento);
}
