package com.stdt.auleweb.data.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface Evento {

    LocalDate getData();

    void setData(LocalDate data);

    LocalTime getOraInizio();

    void setOraInizio(LocalTime oraInizio);

    LocalTime getOraFine();

    void setOraFine(LocalTime oraFine);
    
    String getDescrizione();
    
    void setDescrizione(String descrizione);
    
    String getNome();
    
    void setNome(String nome);
    
    int getId();
    
    void setId(int id);
    
    Tipologia getTipologia();
    
    void setTipologia(Tipologia tipologia);
    
    List<Aula> getAule();
    
    void setAule(List<Aula> aule);
    
    Responsabile getResponsabile();
    
    void setResponsabile(Responsabile responsabile);
    

}
