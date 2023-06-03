package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Amministratore;
import com.stdt.auleweb.framework.data.DataItemImpl;

public class AmministratoreImpl extends DataItemImpl<Integer> implements Amministratore {

    private String username;
    private String password;

    public AmministratoreImpl() {
        super();
        username = "";
        password = "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
