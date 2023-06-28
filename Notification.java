package ggc.core;

import java.io.Serializable;

public class Notification implements Serializable{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Notification's product*/
    private Product _product;
    /*Notification's type*/
    private String _type;
    private double _price;

    /**
    * @param type
    * @param product
    */
    Notification(String type, Product product, double price){
        /* Constructor */
        _product = product;
        _type = type;
        _price = price;
    }

    /**
    * @return product
    */
    Product getProduct(){
        return _product;
    }

    double getPrice(){
        return _price;
    }

    /**
    * @return type
    */
    String getType(){
        return _type;
    }

    /**
    * @return String representing notifications
    */
    @Override
    public String toString(){
        /* it will return the way it will be presented to the user */
        return this.getType() + "|" + this.getProduct().getID() + "|" + (int)Math.round(this.getPrice());
    }
    
}