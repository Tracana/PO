package ggc.core;

class NormalState extends State{
    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;

    /**
    * @param partner 
    */
    NormalState(Partner partner){
        super(partner);
    }

    /**
    * @return NORMAL 
    */
    String getStatus(){
        return "NORMAL";
    }

    /**
    * @param points
    */
    void updatePartnerStatus(int points){
        if(points > 2000 && points <= 25000){
            super._partner.setStatus(new SelectionState(super._partner));
        }
        else if(points > 25000){
            super._partner.setStatus(new EliteState(super._partner));
        }
    }
}