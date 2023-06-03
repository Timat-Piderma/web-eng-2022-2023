package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;

public interface Amministratore extends DataItem<Integer>{
    String getUsername();

    void setUsername(String username);
    
    String getPassword();
    
    void setPassword(String password);
    
}
