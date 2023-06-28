package ggc.core;

public abstract class Sale extends Transaction{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Sale's product*/
    private Product _product;
    /*Sale's partner*/
    private Partner _partner;

    /**
    * @param id
    * @param product
    * @param partner
    * @param quantity
    */
    protected Sale(int id, Product product, Partner partner, int quantity){
        super(id, quantity);
        _product = product;
        _partner = partner;
    }

    public abstract boolean isPaid();

    public abstract String toString();

    abstract double getAmountPaid();

    abstract double getCurrentAmountPaid(Date currentDate);

    abstract boolean paidInTime(Date date);

        /* the following methods are self-explanatory */

    /**
    * @return id partner
    */
    String getIdPartner(){
        return _partner.getID();
    }

    /**
    * @return paymentDate
    */
    Date getPaymentDate(){ 
        return super.getPaymentDate();
    }

    /**
    * @return id product
    */
    String getIdProduct(){
        return _product.getID();
    } 

    /**
    * @return partner 
    */
    @Override
    Partner getPartner(){
        return _partner;
    }
    
    /**
    * @return product
    */
    Product getProduct(){
        return _product;
    }

}