package com.stdt.auleweb.data.proxy;

import com.stdt.auleweb.data.dao.EventoDAO;
import com.stdt.auleweb.data.impl.CorsoImpl;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CorsoProxy extends CorsoImpl implements DataItemProxy{


    protected boolean modified;

    protected DataLayer dataLayer;

    public CorsoProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
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
                super.setEventi(((EventoDAO) dataLayer.getDAO(Evento.class)).getEventiByCorso(this));
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

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
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
