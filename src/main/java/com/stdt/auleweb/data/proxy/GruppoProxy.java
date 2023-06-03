package com.stdt.auleweb.data.proxy;

import com.stdt.auleweb.data.dao.AulaDAO;
import com.stdt.auleweb.data.impl.GruppoImpl;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GruppoProxy extends GruppoImpl implements DataItemProxy {

    protected boolean modified;

    protected DataLayer dataLayer;

    public GruppoProxy(DataLayer d) {
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
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
        this.modified = true;
    }

    @Override
    public List<Aula> getAule() {
        if (super.getAule() == null) {
            try {
                super.setAule(((AulaDAO) dataLayer.getDAO(Aula.class)).getAuleByGruppo(this));
            } catch (DataException ex) {
                Logger.getLogger(GruppoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAule();
    }

    @Override
    public void setAule(List<Aula> aule) {
        super.setAule(aule);
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
