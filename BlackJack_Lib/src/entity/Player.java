/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.Interface.PokerCard;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author OPF_Lep
 */
public class Player implements Serializable {

    int index;
    ArrayList<PokerCard> cards;

    public int getPoint() {
        int point = 0;
        for (PokerCard card : cards) {
            point += card.getPoint();
        }
        return point;
    }

    public ArrayList<PokerCard> getCards() {
        return cards;
    }

    public void setCards(ArrayList<PokerCard> cards) {
        this.cards = cards;
    }

    public Player() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addCard(PokerCard card) {
        cards.add(card);
    }
}
