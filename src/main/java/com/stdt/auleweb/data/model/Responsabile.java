package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;
import java.util.List;

public interface Responsabile extends DataItem<Integer> {

    String getNome();

    void setNome(String nome);

    String getEmail();

    void setEmail(String email);

    List<Evento> getEventi();

    void setEventi(List<Evento> eventi);

    void addEvento(Evento evento);
}
