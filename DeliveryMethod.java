package ggc.core;

public interface DeliveryMethod{
    void send(Observer o, Notification type);
}