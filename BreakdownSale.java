package ggc.core;

import java.util.*;

public class BreakdownSale extends Sale {

    /*BreakdownSale's recipe*/
    private Recipe _recipe;
    /*BreakdownSale's batches*/
    private List<Batch> _batches;
    /*BreakdownSale's amount paid*/
    private double _amountPaid;

    /**
    * @param id
    * @param product
    * @param partner
    * @param quantity
    */
    protected BreakdownSale(int id, Product product, Partner partner, int quantity, Recipe recipe) {
        super(id, product, partner, quantity);
        _recipe = recipe;
    }

    /**
    * @return payment situation
    */
    @Override
    public boolean isPaid(){
        return true;
    }

    /**
    * @return amount paid
    */
    @Override
    double getAmountPaid(){
        return _amountPaid;
    }
    void setAmountPaid(double baseValue){
        _amountPaid = baseValue;
    }

    @Override
    double getCurrentAmountPaid(Date currentDate){
        return _amountPaid;
    }

    /**
    * @return true bc breakdownSale won't pay
    */
    @Override
    boolean paidInTime(Date date){
        return true;
    }

    /**
    * @return batches
    */
    List<Batch> getBatches(){
        return _batches;
    }

    void setBatches(List<Batch> batches){
        _batches = batches;
    }

    /**
    * @return string with info about the batches
    */
    String getInfoRecipe(){
        String info = "";
        for(Component c : _recipe.getComponent()){
            if(info != ""){
                info += "#";
            }
            if(c.getProduct().getMinPrice() != -1){
                info += c.getName() + ":" + Integer.toString(super.getQuantity() * c.getQuantity()) + ":" +
                super.getQuantity() * Math.round(c.getProduct().getMinPrice()*c.getQuantity());
            }
            else
                info += c.getName() + ":" + Integer.toString(super.getQuantity() * c.getQuantity()) + ":" +
                super.getQuantity() * Math.round(c.getProduct().getMaxPrice() * c.getQuantity());
        }
        
        return info;
    }

    /**
    * @return the string that represents the BreakdownSale
    */
    @Override
    public String toString(){
        return "DESAGREGAÇÃO|" + super.getTransactionID() + "|" + super.getIdPartner() + "|" + super.getIdProduct() +
        "|" + super.getQuantity() + "|" + Math.round(super.getBaseValue()) + "|" + Math.round(this.getAmountPaid()) + "|" + 
        super.getPaymentDate() + "|" + this.getInfoRecipe();
    }

}
