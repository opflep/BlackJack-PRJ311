/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.Player;
import java.io.Serializable;
import java.net.Socket;

/**
 *
 * @author OPF_Lep
 */
public class PlayerInfo implements Serializable {

    Player player;
    Socket socket;

    public PlayerInfo(Player player, Socket socket) {
        this.player = player;
        this.socket = socket;
    }

    public PlayerInfo() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
