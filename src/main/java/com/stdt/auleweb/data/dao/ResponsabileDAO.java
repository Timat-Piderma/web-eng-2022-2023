package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.template.model.Responsabile;

public interface ResponsabileDAO {
    
    Responsabile createResponsabile();
    
    Responsabile getResponsabile(Evento evento);
}
