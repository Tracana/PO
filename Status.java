package ggc.core;

public enum Status{
    NORMAL("NORMAL"),
    SELECTION("SELECTION"),
    ELITE("ELITE");

    private String _status;

    /**
    * @param statute
    */
    private Status(String status){
        _status = status;
    }

    /**
    * @return _status
    */
    @Override
    public String toString(){
        return _status;
    }
}