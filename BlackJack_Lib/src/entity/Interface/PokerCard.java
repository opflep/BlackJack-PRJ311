/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.Interface;

import java.io.Serializable;

/**
 *
 * @author OPF_Lep
 */
public class PokerCard implements Serializable {

    /**
     *
     */
    int value;

    public PokerCard(int value) {
        this.value = value;
    }

    public int getPoint() {
        if (value <= 10) {
            return value;
        } else {
            return 10;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getValueString() {
        return null;
    }

    ;
 public String getURL() {
        return "img/" + getClass().getSimpleName() + "/" + value + ".png";
    }

}
