package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;
import java.util.List;

public interface Corso extends DataItem<Integer> {

    String getNome();

    void setNome(String nome);

    List<Evento> getEventi();

    void setEventi(List<Evento> eventi);

    void addEvento(Evento evento);
}
