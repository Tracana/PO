package ggc.core;

import java.util.ArrayList;
import java.util.List;

public class AggregateProduct extends Product{

    /*AggregateProduct's batches*/
    private List<Batch> _batches = new ArrayList<>();
    /*AggregateProduct's recipe*/
    private Recipe _recipe;

    /**
    * @param id
    * @param maxPrice
    */
    protected AggregateProduct(String id, Recipe recipe){
        /* Constructor */
        super(id);
        _recipe = recipe;
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
                if(quantity <= _recipe.getComponent().size()){ 
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * @return "AGGREGATEPRODUCT"
    */
    @Override
    public String getType(){
        return "AGGREGATEPRODUCT";
    }

    /**
    * @return recipe
    */
    public Recipe getRecipe(){
        return _recipe;
    }

    /**
    * @return String of the aggregate product with recipe
    */
    @Override
    public String toString(){
        /* it will return the way it will be presented to the user */
        return super.getID() + "|" + (int)super.getMaxPrice() + "|" + super.getStock() + "|" + _recipe.toString();
    }
}