package ggc.core;

class SelectionState extends State{
    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;

    /**
    * @param partner 
    */
    SelectionState(Partner partner){
        super(partner);
    }

    /**
    * @return SELECTION
    */
    String getStatus(){
        return "SELECTION";
    }

    /**
    * @param points
    */
    void updatePartnerStatus(int points){
        if(points > 25000){
            super._partner.setStatus(new EliteState(super._partner));
        }
        else if(points <= 2000){
            super._partner.setStatus(new NormalState(super._partner));
        }
    }
}