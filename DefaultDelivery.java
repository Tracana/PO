package ggc.core;

import java.io.Serializable;

public class DefaultDelivery implements DeliveryMethod, Serializable{

    /* DefaultDelivery's default*/
    private static DefaultDelivery _default;

    /* Constructor */
    private DefaultDelivery(){}

    /**
    * @return default
    */
    public static DefaultDelivery getDefault(){
        if(_default == null){
            _default = new DefaultDelivery();
        }
        return _default;
    }

    /**
    * @param observer
    * @param type
    */
    @Override
    public void send(Observer o, Notification type) {
        if(type.getType().equals("BARGAIN")){
            o.updateBargainNotification(type.getProduct(), type.getPrice());
        }
        else{
            o.updateNewNotification(type.getProduct(), type.getPrice());
        }
    }
    
}
