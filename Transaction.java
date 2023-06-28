package ggc.core;

import java.io.Serializable;
import java.util.*;

public abstract class Transaction implements Serializable{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Transaction's id*/
    private int _id;
    /*Transaction's paymentDate*/
    private Date _paymentDate;
    /*Transaction's baseValue*/
    private double _baseValue;
    /*Transaction's quantity*/
    private int _quantity;

    /**
    * @param id
    * @param quantity
    */
    Transaction(int id, int quantity){
        _id = id;
        _quantity = quantity;
    }

    public abstract boolean isPaid();

    public abstract String toString();

    abstract boolean paidInTime(Date date);

    abstract Partner getPartner();

    @Override
    public int hashCode(){
        return Objects.hash(_id);
    }

        /* the following methods are self-explanatory */

    /**
    * @param baseValue
    */  
    void setBaseValue(double baseValue) {
        _baseValue = baseValue;
    }

    /**
    * @param date
    */
    void setPaymentDate(int date) {
        _paymentDate = new Date(date);
    }

    /**
    * @return paymentDate
    */
    Date getPaymentDate(){ 
        return _paymentDate;
    }

    /**
    * @return id
    */
    int getTransactionID(){
        return _id;
    }

    /**
    * @return base Value
    */
    double getBaseValue(){
        return _baseValue;
    }

    /**
    * @return quantity
    */
    int getQuantity(){
        return _quantity;
    }

}