package ggc.core;

import java.util.ArrayList;
import java.util.List;

public class SimpleProduct extends Product{

    /*SimpleProduct's batches*/
    private List<Batch> _batches = new ArrayList<>();

    /**
    * @param id
    * @param maxPrice
    */
    protected SimpleProduct(String id){
        /* Constructor */
        super(id);
    }

    /**
    * @param quantity
    * @param partner
    * @return true or false
    */
    @Override
    protected boolean checkQuantity(int quantity, Partner partner){
        /* it will check if it exists a certain quantity of a product and if 
        it does it will return true, in case not it will return false */
        for(Batch b : _batches){
            if(b.getIDPartner().equals(partner.toString())){
                if(quantity <= super.getStock()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * @return "SIMPLEPRODUCT"
    */
    @Override
    String getType(){
        return "SIMPLEPRODUCT";
    }

    /**
    * @return String of the simple product
    */
    @Override
    public String toString(){
        /* it will return the way it will be presented to the user */
        return super.getID() + "|" + (int)Math.round(super.getMaxPrice()) + "|" + super.getStock();
    }

} 