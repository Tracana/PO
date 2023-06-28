package ggc.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable{

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*Recipe's alpha*/
    private double _alpha;
    /*Recipe's list of components*/
    private List<Component> _components = new ArrayList<>();

    /**
    * @param alpha
    */
    Recipe(double alpha, List<Component> components){
        /* Constructor */
        _alpha = alpha;
        _components = new ArrayList<>(components);
    }

    /**
    * @return String with the alpha and componets of the recipe
    */
    @Override
    public String toString(){
        /* it will return the way it will be presented to the user */
        String components = "";
        for(Component c : _components){
            if(components != ""){
                components += "#";
            }
            components += c.getName() + ":" + Integer.toString(c.getQuantity());
        }
        return components;
    }

            /* the following methods are self-explanatory */
    /**
    * @return alpha
    */
    double getAlpha(){
        return _alpha;
    }

    /**
    * @return String componets of the recipe
    */
    List<Component> getComponent(){
        return _components;
    }

}