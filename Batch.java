package ggc.core;

import java.io.Serializable;

/**
 * Class Batch that has all the information of each batch
*/
public class Batch implements Serializable{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Batch's price*/
    private double _price;
    /*Batch's quantity*/
    private int _quantity;
    /*Batch's product*/
    private Product _product;
    /*Batch's partner*/
    private Partner _partner;

    /**
    * @param product
    * @param partner
    * @param price
    * @param quantity
    */
    Batch(Product product, Partner partner, double price, int quantity){ 
        /* Constructor */
        _product = product;
        _partner = partner;
        _price = price;
        _quantity = quantity;
    }

    /**
    * @return newBatch
    */
    protected Batch makeCopy(){
        /* it will make a copy and return a copy of an existing batch */
        Batch newBatch = new Batch(getProduct(), getPartner(), getPrice(), getStock());
        return newBatch;
    }

    /**
    * @param stock
    */
    void addStock(int stock){
        _quantity += stock;
    }

    /**
    * @param stock
    */
    void removeStock(int stock){
        _quantity -= stock;
    }

    /**
    * @return String representing batches
    */
    @Override
    public String toString(){
        return this.getIDProduct() + "|" + this.getIDPartner() + "|" + (int)this.getPrice() + "|" + this.getStock();
    }

        /* the following methods are self-explanatory */

    /**
    * @return product 
    */
    Product getProduct(){
        return _product;
    }

    /**
    * @return partner
    */
    Partner getPartner(){
        return _partner;
    }

    /**
    * @return product id
    */
    String getIDProduct(){
        return _product.getID();
    }

    /**
    * @return partner id
    */
    String getIDPartner(){
        return _partner.getID();
    }

    /**
    * @return price
    */
    double getPrice(){
        return _price;
    }

    /**
    * @return quantity
    */
    int getStock(){
        return _quantity;
    }

}