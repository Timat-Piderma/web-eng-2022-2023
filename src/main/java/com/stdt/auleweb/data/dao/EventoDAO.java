package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.data.model.Responsabile;
import java.time.LocalDate;
import java.util.List;

public interface EventoDAO {

    Evento createEvento() throws DataException;
    
    Evento getEvento(int evento_key) throws DataException;
    
    List<Evento> getEventiByCorso (Corso corso) throws DataException;
    
    List<Evento> getEventiByResponsabile(Responsabile responsabile) throws DataException;

    List<Evento> getEventi() throws DataException;

    List<Evento> getEventi(Aula aula) throws DataException;
    
    List<Evento> getEventiBySettimana(Aula aula, LocalDate giorno) throws DataException;

    List<Evento> getEventiByGiorno(Gruppo gruppo, LocalDate giorno) throws DataException;
    
    List<Evento> getEventiNextThreeHours(Gruppo gruppo) throws DataException;
    
    List<Evento> getEventiBySettimanaAndCorso(Corso corso, LocalDate giorno) throws DataException;
    
    void storeEvento(Evento evento) throws DataException;
    
  

}
