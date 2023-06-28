package ggc.core;

public class Acquisition extends Transaction{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Acquisition's partner*/
    private Partner _partner;
    /*Acquisition's product*/
    private Product _product;

    /**
    * @param id
    * @param product
    * @param partner
    * @param quantity
    */
    protected Acquisition(int id, Product product, Partner partner, int quantity, double amountPaid, Date date){
        super(id, quantity);
        super.setBaseValue(amountPaid);
        _product = product;
        _partner = partner;
        super.setPaymentDate(date.now());
    }

    /**
    * @return id partner
    */
    String getIdPartner(){
        return _partner.getID();
    }

    /**
    * @return partner 
    */
    @Override
    Partner getPartner(){
        return _partner;
    }

    /**
    * @return date
    */
    @Override
    boolean paidInTime(Date date){
        return true;
    }

    /**
    * @return id product
    */
    String getIdProduct(){
        return _product.getID();
    } 

    /**
    * @return true
    */
    @Override
    public boolean isPaid(){
        return true;
    }

    /**
    * @return string represents the acquisition
    */
    public String toString(){
        return "COMPRA|" + super.getTransactionID() + "|" + this.getIdPartner() + "|" + this.getIdProduct() +
        "|" + super.getQuantity() + "|" + (int)Math.round(super.getBaseValue()) + "|" + super.getPaymentDate().now();
    }
}