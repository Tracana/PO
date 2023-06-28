package ggc.core;

import java.io.Serializable;
import java.util.*;

/**
 * Class Partner that has all the information of each partner
*/
public abstract class Product implements Serializable{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Product's maxPrice*/
    private double _maxPrice;
    /*Product's id*/
    private String _id;
    /*Product's stock*/
    private int _stock;
    /*Product's batch*/
    private List<Batch> _batches;
    /*Product's partners to notificate*/
    private List<Observer> _observers;

    /**
    * @param id
    * @param maxPrice
    */
    protected Product(String id){
        /* Constructor */
        _id = id;
        _maxPrice =  0.0;
        _stock = 0;
        _observers = new ArrayList<>();
        _batches = new ArrayList<>();
    }

    public abstract String toString();

    abstract String getType();

    protected abstract boolean checkQuantity(int quantity, Partner partner);

    @Override
    public int hashCode(){
        return Objects.hash(_id);
    }

    /**
    * @return true or false
    */
    @Override
    public boolean equals(Object object){
        return object instanceof Product && ((Product)object).getID().equals(_id);
    }

    /**
    * @param stock
    */
    void updateStock(int stock){
        /* it will increase the stock of a product*/
        _stock += stock;
    }

    /**
    * @param stock
    */
    void removeStock(int stock){
        /* it will remove the stock of a product*/
        _stock -= stock;
    }

    /**
    * @param price
    */
    void updateMaxPrice(Double price){
        if(_maxPrice < price)
            _maxPrice = price;
    }

    /**
    * @param batch
    */
    void addBatch(Batch batch){
        _batches.add(batch);
    }

        /* the following methods are self-explanatory */

    /**
    * @return minimum price
    */
    double getMinPrice(){
        double minPrice = -1;
        for(Batch b : getBatches()){
            if(minPrice == -1){
                minPrice = b.getPrice();
            }
            else if(minPrice > b.getPrice()){
                minPrice = b.getPrice();
            }
        }
        return minPrice;
    }

    /**
    * @return batches
    */
    List<Batch> getBatches(){
        return _batches;
    }

    /**
    * @return id
    */
    String getID(){
        return _id;
    }

    /**
    * @return stock
    */
    int getStock(){
        return _stock;
    }

    /**
    * @return maxPrice 
    */
    double getMaxPrice(){
        return _maxPrice;
    }

    /**
    * @return list of observers aka partners
    */
    List<Observer> getObservers(){
        return _observers;
    }

        /* the following methods are related to the Notifications and Observable */

    /**
    * @param Partner (Observer)
    */
    void addObserver(Observer o){
        _observers.add(o);
    }

    /**
    * @param Partner (Observer)
    */
    void removeObserver(Observer o){
        _observers.remove(o);
    }

    /**
    * @param type
    * @param product
    */
    void notifyAllObservers(String type, Product product, double price){
        for(Observer p : _observers){
            switch(type){
                case "NEW":
                    p.updateNewNotification(product, price);
                    break;
                case "BARGAIN":
                    p.updateBargainNotification(product, price);
                    break;
            }      
        }
    }

}