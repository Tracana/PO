package ggc.core;

import java.io.Serializable;
import java.util.*;

public class Partner implements Serializable, Observer{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Partner's name*/
    private String _name;
    /*Partner's address*/
    private String _address;
    /*Partner's id*/
    private String _id;
    /*Partner's status*/
    private State _status;
    /*Partner's points*/
    private int _points;
    /*Partner's value of the aqcquisitions*/
    private Double _acquisitionsValue;
    /*Partner's sales made*/
    private int _salesMade;
    /*Partner's sales paid*/
    private int _salesPaid;
    /*Partner's notification*/
    private List<Notification> _notifications;
    /*Partner's sales*/
    private List<Sale> _sales;
    /*Partner's acquisitions*/
    private List<Acquisition> _acquisitions;
    /*Partner's sales paid*/
    private List<Sale> _allSalesPaid;
    private DeliveryMethod _deliveryMethod;

    /**
    * @param name
    * @param address
    * @param id
    */
    Partner(String id, String name, String address){
        /* Constructor */
        _name = name;
        _address = address;
        _id = id;
        _points = 0;
        _status = new NormalState(this);
        _salesMade = 0;
        _salesPaid = 0;
        _acquisitionsValue = 0.0;
        _notifications = new ArrayList<>();
        _sales = new ArrayList<>();
        _acquisitions = new ArrayList<>();
        _allSalesPaid = new ArrayList<>(); 
        _deliveryMethod = DefaultDelivery.getDefault();
    }

    @Override
    public int hashCode(){
        return Objects.hash(_id);
    }

    /**
    * @return true or false
    */
    @Override
    public boolean equals(Object object){
        return object instanceof Partner && ((Partner)object).getID().equals(_id);
    }

    /**
    * @param points
    */
    void addPoints(int points){
        _points += points;
    }

    /**
    * @param points
    */
    void setPoints(double points){
        _points = (int)Math.round(points);
    }

    /**
    * @param points
    */
    void removePoints(int points){
        _points -= points;
    }

    void updateStatus(){
        _status.updatePartnerStatus((int)Math.round(getPoints()));
    }

    /**
    * @return String representing partners
    */
    @Override
    public String toString(){
        /* it will return the way it will be presented to the user */
        return this.getID() + "|" +  this.getName() + "|" + this.getAddress()  + "|" + this.getStatus() + "|" +
        this.getPoints() + "|" + (int)Math.round(this.getAcquisitionsValue()) + "|" + this.getSalesMade() + "|"
        + this.getSalesPaid();
    }

        /* the following methods are self-explanatory */

    /**
    * @return id
    */
    String getID(){
        return _id;
    }

    /**
    * @return address
    */
    String getAddress(){
        return _address;
    }

    /**
    * @return name
    */
    String getName(){
        return _name;
    }

    /**
    * @return status
    */
    String getStatus(){
        return _status.getStatus();
    }

    /**
    * @param status
    */
    void setStatus(State status){
        _status = status;
    }

    /**
    * @param value
    */
    void addAcquisitionValue(Double value){
        _acquisitionsValue += value; 
    }

    /**
    * @return points
    */
    int getPoints(){
        return _points;
    }

    /**
    * @return acquisitions Value
    */
    double getAcquisitionsValue(){
        return _acquisitionsValue;
    }

    /**
    * @return sales Made
    */
    int getSalesMade(){
        return _salesMade;
    }
    
    /**
    * @param paid
    */
    void addSalesPaid(int paid){
        _salesPaid += paid;
    }

    /**
    * @return sales paid
    */
    int getSalesPaid(){
        return _salesPaid;
    }

    /**
    * @return all sales paid
    */
    List<Sale> getAllSalesPaid(){
        return _allSalesPaid;
    }
    
    /**
    * @return all notifications 
    */
    List<Notification> getNotifications(){
        return _notifications;
    }

    /**
    * @return all sales
    */
    List<Sale> getSales(){
        return _sales;
    }

    /**
    * @return all acquisitions
    */
    List<Acquisition> getAcquisitions(){
        return _acquisitions;
    }

    /**
    * @return all string of all acquisitions
    */
    List<String> getAcquisitionsString(){
        List<String> acquisitionsString = new ArrayList<>();

        for(Transaction acquisition : _acquisitions){
            acquisitionsString.add(acquisition.toString());
        }
        return acquisitionsString;
    }

    /**
    * @param currentDate
    */
    double getPartnerAccountingBalance(Date currentDate){
        double balance = 0.0;
        for(Sale s : _sales){
            if(s instanceof SaleByCredit){
                if(!s.isPaid()){
                    //System.out.println(s.getCurrentAmountPaid(currentDate));
                    balance += s.getCurrentAmountPaid(currentDate);
                }
            }
        }
        return balance;
    }

    /**
    * @return all string of all sales
    */
    List<String> getSalesString(){
        List<String> salesString = new ArrayList<>();

        for(Transaction sale : _sales){
            salesString.add(sale.toString());
        }
        return salesString;
    }

        /* the following methods are related to the Notifications and Observer */

    /**
    * @param product
    */
    @Override
    public void updateNewNotification(Product product, double price){
        /* Observer */
        _notifications.add(new Notification("NEW", product, price));
    }

    /**
    * @param product
    */
    @Override
    public void updateBargainNotification(Product product, double price){
        /* Observer */
        _notifications.add(new Notification("BARGAIN", product, price));
    }

    void cleanNotification(){
        _notifications.clear();
    }
    
    /**
    * @param Notification 
    */
    void Default(Notification type){
        _deliveryMethod.send(this, type);
    }

    /**
    * @param DeliveryMethod
    */
    void setDeliveryMethod(DeliveryMethod dm){
        _deliveryMethod = dm;
    }

        /* the following methods are related to the Transactions */

    /**
    * @param sale
    */
    void addSale(Sale sale){
        _sales.add(sale);
        _salesMade += sale.getBaseValue();
    }

    /**
    * @param sale
    */
    void addBreakdown(Sale sale){
        _sales.add(sale);
    }

    /**
    * @param acquisition
    */
    void addAcquisition(Acquisition acquisition){
        _acquisitions.add(acquisition);
    }
    
}




