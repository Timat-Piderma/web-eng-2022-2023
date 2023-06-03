package com.stdt.auleweb.data.proxy;

import com.stdt.auleweb.data.dao.AulaDAO;
import com.stdt.auleweb.data.impl.PosizioneImpl;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PosizioneProxy extends PosizioneImpl implements DataItemProxy {

    protected boolean modified;

    protected DataLayer dataLayer;

    public PosizioneProxy(DataLayer d) {
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
    public void setAule(List<Aula> aule) {
        super.setAule(aule);
        this.modified = true;
    }

    @Override
    public List<Aula> getAule() {
        if (super.getAule() == null) {
            try {
                super.setAule(((AulaDAO) dataLayer.getDAO(Aula.class)).getAuleByPosizione(this));
            } catch (DataException ex) {
                Logger.getLogger(PosizioneProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAule();
    }

    @Override
    public void addAula(Aula aula) {
        super.addAula(aula);
        this.modified = true;
    }

    @Override
    public void setLuogo(String nome) {
        super.setLuogo(nome);
        this.modified = true;
    }

    @Override
    public void setEdificio(String edificio) {
        super.setEdificio(edificio);
        this.modified = true;
    }

    @Override
    public void setPiano(String piano) {
        super.setPiano(piano);
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
