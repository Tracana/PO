package ggc.core;

import java.io.Serializable;

public class Component implements Serializable{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Component's quantity*/
    private int _quantity;
    /*Component's product*/
    private Product _product;

    /**
    * @param quantity
    * @param product
    */
    Component(int quantity, Product product){
        /* Constructor */
        _quantity = quantity;
        _product = product;
    }

        /* the following methods are self-explanatory */
        
    /**
    * @return quantity
    */
    int getQuantity(){
        return _quantity;
    }

    Product getProduct(){
        return _product;
    }

    /**
    * @return id of the product
    */
    String getName(){
        return _product.getID();
    }

}