package ggc.core;

import java.io.Serializable;

abstract class State implements Serializable {
    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*State needs to be associated with the partners*/
    protected Partner _partner;

    /**
    * @param partner 
    */
    State(Partner partner){
        _partner = partner;
    }

    abstract String getStatus();
    abstract void updatePartnerStatus(int points);
}