package ggc.core;

interface Observable {

    public void add(Observer obs);
    public void remove(Observer obs);

}