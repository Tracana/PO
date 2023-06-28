package ggc.core;

interface Observer{

    public void updateNewNotification(Product product, double price);
    public void updateBargainNotification(Product product, double price);

}