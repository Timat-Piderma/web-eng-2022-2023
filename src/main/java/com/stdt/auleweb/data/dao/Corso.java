package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Evento;

public interface Corso {
    
    Corso createCorso();
    
    Corso getCorso(Evento evento);
}
