package com.stdt.auleweb.data.proxy;

import com.stdt.auleweb.data.dao.AulaDAO;
import com.stdt.auleweb.data.dao.CorsoDAO;
import com.stdt.auleweb.data.dao.ResponsabileDAO;
import com.stdt.auleweb.data.impl.EventoImpl;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Responsabile;
import com.stdt.auleweb.data.model.Tipologia;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import java.sql.Date;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventoProxy extends EventoImpl implements DataItemProxy {

    protected boolean modified;
    protected int aula_key = 0;
    protected int responsabile_key = 0;
    protected int corso_key = 0;

    protected DataLayer dataLayer;

    public EventoProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
        this.aula_key = 0;
        this.responsabile_key = 0;
        this.corso_key = 0;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public Responsabile getResponsabile() {
        if (super.getResponsabile() == null && responsabile_key > 0) {
            try {
                super.setResponsabile(((ResponsabileDAO) dataLayer.getDAO(Responsabile.class)).getResponsabile(responsabile_key));
            } catch (DataException ex) {
                Logger.getLogger(EventoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getResponsabile();
    }

    @Override
    public Corso getCorso() {
        if (super.getCorso() == null && corso_key > 0) {
            try {
                super.setCorso(((CorsoDAO) dataLayer.getDAO(Corso.class)).getCorso(corso_key));
            } catch (DataException ex) {
                Logger.getLogger(EventoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCorso();
    }

    @Override
    public void setGiorno(Date giorno) {
        super.setGiorno(giorno);
        this.modified = true;

    }

    @Override
    public void setOraInizio(Time oraInizio) {
        super.setOraInizio(oraInizio);
        this.modified = true;
    }

    @Override
    public void setOraFine(Time oraFine) {
        super.setOraFine(oraFine);
        this.modified = true;
    }

    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
        this.modified = true;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setTipologia(Tipologia tipologia) {
        super.setTipologia(tipologia);
        this.modified = true;
    }

    @Override
    public Aula getAula() {
        if (super.getAula() == null && aula_key > 0) {
            try {
                super.setAula(((AulaDAO) dataLayer.getDAO(Aula.class)).getAula(aula_key));
            } catch (DataException ex) {
                Logger.getLogger(EventoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAula();
    }

    //METODI DEL PROXY
    //PROXY-ONLY METHODS
    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    public void setAulaKey(int aula_key) {
        this.aula_key = aula_key;
        super.setAula(null);
    }

    public void setResponsabileKey(int responsabile_key) {
        this.responsabile_key = responsabile_key;
        super.setResponsabile(null);
    }

    public void setCorsoKey(int corso_key) {
        this.corso_key = corso_key;
        super.setCorso(null);
    }
}
