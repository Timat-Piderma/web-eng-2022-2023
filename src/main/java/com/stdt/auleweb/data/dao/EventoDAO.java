package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.rest.model.Aula;
import com.stdt.auleweb.rest.model.Evento;
import java.util.List;

public interface EventoDAO {
    
    Evento createEvento();
    
    List<Evento> getEventi();
    
    List<Evento> getEventi(Aula aula);
}
