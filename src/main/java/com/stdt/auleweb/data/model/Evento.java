package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;
import java.sql.Date;
import java.sql.Time;

public interface Evento extends DataItem<Integer> {

    Date getGiorno();

    void setGiorno(Date data);

    Time getOraInizio();

    void setOraInizio(Time oraInizio);

    Time getOraFine();

    void setOraFine(Time oraFine);

    String getDescrizione();

    void setDescrizione(String descrizione);

    String getNome();

    void setNome(String nome);

    Tipologia getTipologia();

    void setTipologia(Tipologia tipologia);

    Aula getAula();

    void setAula(Aula aula);

    Responsabile getResponsabile();

    void setResponsabile(Responsabile responsabile);

    Corso getCorso();

    void setCorso(Corso corso);

    void removeCorso();
}
