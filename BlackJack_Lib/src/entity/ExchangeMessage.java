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
public class ExchangeMessage implements Serializable {

    public static final int PLAYER_START = 0;
    public static final int PLAYER_HIT = 1;
    public static final int PLAYER_STAND = 2;
    public static final int SERVER_UPDATE_CARD_TO_PLAYER = 3;
    public static final int SERVER_SEND_RESULT = 4;
    public static final int CLIENT_SEND_REMAKE = 5;
    public static final int CLIENT_SEND_CARD_TO_PLAYER = 6;
    public static final int SERVER_SEND_INDEX = 7;
    public static final int CLIENT_CF_INDEX = 8;

    ArrayList<PokerCard> player1Card = new ArrayList<>();
    ArrayList<PokerCard> player2Card = new ArrayList<>();
    ArrayList<Player> players;
    PokerCard cardToAdd;
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    private Player player;

    private int command;
//    private int start=0;
//    private int stand=0;

    public ExchangeMessage() {
    }

    public ExchangeMessage(int command) {
        this.command = command;
    }

    public ExchangeMessage(Player player) {
        this.player = player;
    }

    public ArrayList<PokerCard> getPlayer1Card() {
        return player1Card;
    }

    public void setPlayer1Card(ArrayList<PokerCard> player1Card) {
        this.player1Card = player1Card;
    }

    public ArrayList<PokerCard> getPlayer2Card() {
        return player2Card;
    }

    public void setPlayer2Card(ArrayList<PokerCard> player2Card) {
        this.player2Card = player2Card;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public PokerCard getCardToAdd() {
        return cardToAdd;
    }

    public void setCardToAdd(PokerCard cardToAdd) {
        this.cardToAdd = cardToAdd;
    }

}
