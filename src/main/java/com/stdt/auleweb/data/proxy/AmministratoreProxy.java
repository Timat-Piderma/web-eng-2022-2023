package com.stdt.auleweb.data.proxy;

import com.stdt.auleweb.data.impl.AmministratoreImpl;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;

public class AmministratoreProxy extends AmministratoreImpl implements DataItemProxy {
    protected boolean modified;
    protected DataLayer dataLayer;
    
    public AmministratoreProxy(DataLayer d) {
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
    public void setUsername(String username) {
        super.setUsername(username);
        this.modified = true;
    }
    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        this.modified = true;
    }
    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified=dirty;
    }
    
}
