package com.stdt.auleweb.data.dao
        ;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.data.model.Aula;

public interface PosizioneDAO {

    Posizione createPosizione();
    
    Posizione getPosizione();
    
    Posizione getPosizione(Aula aula);
}
