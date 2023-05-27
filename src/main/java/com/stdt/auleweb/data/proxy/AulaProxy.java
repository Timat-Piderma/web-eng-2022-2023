package com.stdt.auleweb.data.proxy;

import com.stdt.auleweb.data.dao.AttrezzaturaDAO;
import com.stdt.auleweb.data.dao.EventoDAO;
import com.stdt.auleweb.data.dao.GruppoDAO;
import com.stdt.auleweb.data.dao.PosizioneDAO;
import com.stdt.auleweb.data.impl.AulaImpl;
import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import com.stdt.auleweb.data.model.Posizione;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AulaProxy extends AulaImpl implements DataItemProxy {

    protected boolean modified;
    protected int posizione_key = 0;
    protected int gruppo_key = 0;

    protected DataLayer dataLayer;

    public AulaProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
        this.posizione_key = 0;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    public void setGruppoKey(int gruppo_key) {
        this.gruppo_key = gruppo_key;
        super.setGruppo(null);
    }

    public void setPosizioneKey(int posizione_key) {
        this.posizione_key = posizione_key;
        super.setPosizione(null);
    }

    @Override
    public void setPosizione(Posizione posizione) {
        super.setPosizione(posizione);
        this.posizione_key = posizione.getKey();
        this.modified = true;

    }

    @Override
    public Posizione getPosizione() {
        if (super.getPosizione() == null && posizione_key > 0) {
            try {
                super.setPosizione(((PosizioneDAO) dataLayer.getDAO(Posizione.class)).getPosizione(posizione_key));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getPosizione();
    }

    @Override
    public void setCapienza(int capienza) {
        super.setCapienza(capienza);
        this.modified = true;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setNumeroPreseElettriche(int numeroPreseElettriche) {
        super.setNumeroPreseElettriche(numeroPreseElettriche);
        this.modified = true;
    }

    @Override
    public void setNumeroPreseRete(int numeroPreseRete) {
        super.setNumeroPreseRete(numeroPreseRete);
        this.modified = true;
    }

    @Override
    public void setNote(String note) {
        super.setNote(note);
        this.modified = true;
    }

    @Override
    public void setEmailResponsabile(String emailResponsabile) {
        super.setEmailResponsabile(emailResponsabile);
        this.modified = true;
    }

    @Override
    public void setAttrezzature(List<Attrezzatura> attrezzature) {
        super.setAttrezzature(attrezzature);
        this.modified = true;
    }

    @Override
    public List<Attrezzatura> getAttrezzature() {
        if (super.getAttrezzature() == null) {
            try {
                super.setAttrezzature(((AttrezzaturaDAO) dataLayer.getDAO(Attrezzatura.class)).getAttrezzature(this));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAttrezzature();
    }

    @Override
    public void addAttrezzatura(Attrezzatura attrezzatura) {
        super.addAttrezzatura(attrezzatura);
        this.modified = true;
    }

    @Override
    public void setGruppo(Gruppo gruppo) {
        super.setGruppo(gruppo);
        this.modified = true;
    }

    @Override
    public Gruppo getGruppo() {
        if (super.getGruppo() == null) {
            try {
                super.setGruppo(((GruppoDAO) dataLayer.getDAO(Gruppo.class)).getGruppo(this));
            } catch (DataException ex) {
                //Logger.getLogger(GruppoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getGruppo();
    }

    @Override
    public void setEventi(List<Evento> eventi) {
        super.setEventi(eventi);
        this.modified = true;
    }

    @Override
    public List<Evento> getEventi() {
        if (super.getEventi() == null) {
            try {
                super.setEventi(((EventoDAO) dataLayer.getDAO(Evento.class)).getEventi(this));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getEventi();
    }

    @Override
    public void addEvento(Evento evento) {
        super.addEvento(evento);
        this.modified = true;
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

}
