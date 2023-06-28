package ggc.core;

class EliteState extends State{
    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;

    /**
    * @param partner 
    */
    EliteState(Partner partner){
        super(partner);
    }

    /**
    * @return ELITE
    */
    String getStatus(){
        return "ELITE";
    }

    /**
    * @param points
    */
    void updatePartnerStatus(int points){
        if(points > 2000 && points <= 25000){
            super._partner.setStatus(new SelectionState(super._partner));
        }
        else if(points <= 2000){
            super._partner.setStatus(new NormalState(super._partner));
        }
    }
}