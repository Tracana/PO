package ggc.core;

import java.io.Serializable;

public class Date implements Serializable{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Dates's current days*/
    private int _days;

    Date(){
        /* Constructor */
        _days = 0;
    }

    /**
    * @param days
    */
    Date(int days) {
        /* Constructor */
        _days = days;
    }

    /**
    * @return days
    */
    public int now(){
        return _days;
    }

    /**
    * @return string of days
    */
    @Override
    public String toString(){
        return "" + _days;
    }

    /**
    * @param days
    * @return Date
    */
    public Date add(int days){
        if(days > 0){
            _days += days;
        }
     return this;
    }

    /**
    * @param Date (other)
    * @return difference between current date and the other
    */
    public int difference(Date other){
        return (_days - other.now());
    }
}