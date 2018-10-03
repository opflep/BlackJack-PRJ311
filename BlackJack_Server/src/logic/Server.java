/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import entity.ExchangeMessage;
import entity.Interface.ClubCard;
import entity.Interface.DiamondCard;
import entity.Interface.HeartCard;
import entity.Interface.PokerCard;
import entity.Interface.SpadesCard;
import entity.Player;
import entity.PlayerInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;

/**
 *
 * @author OPF_Lep
 */
public class Server {

    int indexOfPlayers = 0;
    int start = 0;
    int stand = 0;
    int remake = 0;
    boolean set = false;
    ServerSocket server;
    ArrayList<PlayerInfo> playersInfo = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<PokerCard> desk = new ArrayList<>();
    ArrayList<Integer> index = new ArrayList<>();
    ArrayList<PokerCard> temp = new ArrayList<>();
    ExchangeMessage message;

    public Server() {
        try {
            index.add(1);
            server = new ServerSocket(9999);
            log("Server Started.");
            ConnectionAcceptor acceptor = new ConnectionAcceptor();
            acceptor.start();
            log("Server stated accepting connection....");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class ConnectionAcceptor extends Thread {

        @Override
        public void run() {
            while (indexOfPlayers < 2) {
                try {
                    indexOfPlayers++;
                    Socket socket = server.accept();
                    Player player = new Player();
                    player.setIndex(indexOfPlayers);
                    PlayerInfo client = new PlayerInfo();
                    client.setPlayer(player);
                    client.setSocket(socket);
                    playersInfo.add(client);
                    log("client " + socket.getInetAddress().getHostAddress().toString()
                            + " has connected to Server.");
                    if (indexOfPlayers != 2) {
                        System.out.println("Waiting other player");
                    } else {
                        PlayingHost thread_1 = new PlayingHost(playersInfo.get(0));
                        thread_1.start();
                        PlayingHost thread_2 = new PlayingHost(playersInfo.get(1));
                        thread_2.start();
                    }

                    //  }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }

    class PlayingHost extends Thread {

        PlayerInfo current;
        boolean isRunning = true;

        public PlayingHost(PlayerInfo player) {
            this.current = player;
            desk = createNewDesk(desk);
            setNewCardsToPlayers();
        }

        @Override
        public void run() {

            try {

                int tempInd = 0;
                OutputStream os = current.getSocket().getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);

                while (isRunning) {
                    InputStream is = current.getSocket().getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    ExchangeMessage message = (ExchangeMessage) ois.readObject();
                    switch (message.getCommand()) {
                        case ExchangeMessage.PLAYER_START:

                            start++;
                            while (start != 2) {
                                System.out.println(start);
                            }
                            System.out.println("2 player ok");

                            ExchangeMessage sendIndex = messageSendIndexs();
                            System.out.println(sendIndex);

                            oos.writeObject(sendIndex);
                            Thread.sleep(200);
                            //     break;
                            //    case ExchangeMessage.CLIENT_CF_INDEX:
//                            os = current.getSocket().getOutputStream();
//                            ObjectOutputStream oos_card = new ObjectOutputStream(os);
                            sendCard(current);
                            System.out.println("Sent card");

                            break;
                        case ExchangeMessage.PLAYER_HIT:

                            int index = message.getIndex();
                            ExchangeMessage addCardMessage = new ExchangeMessage();
                            addCardMessage.setCardToAdd(desk.remove(0));
                            addCardMessage.setIndex(index);
                            addCardMessage.setCommand(ExchangeMessage.SERVER_UPDATE_CARD_TO_PLAYER);
                            for (PlayerInfo playerInfo : playersInfo) {
                                OutputStream os_x = playerInfo.getSocket().getOutputStream();
                                oos = new ObjectOutputStream(os_x);
                                oos.writeObject(addCardMessage);
                            }
                            //oos.writeObject(addCardMessage);
                            break;
                        case ExchangeMessage.PLAYER_STAND:
                            System.out.println("Got Stand");
                            if (message.getIndex() != tempInd) {
                                stand++;
                                tempInd = message.getIndex();
                            }
                            while (stand < 2) {
                                Thread.sleep(1000);
                            }
                            System.out.println("2stand");
                            ExchangeMessage endGameMessage = new ExchangeMessage();
                            endGameMessage.setCommand(ExchangeMessage.SERVER_SEND_RESULT);
                            OutputStream os_x = current.getSocket().getOutputStream();
                            oos = new ObjectOutputStream(os_x);
                            oos.writeObject(endGameMessage);
                            tempInd = 0;
                            break;
                        case ExchangeMessage.CLIENT_SEND_REMAKE:
                            remake++;
                            while (remake != 2) {
                                System.out.println("x");
                            }
                            System.out.println(remake);

                            setNewCardsToRemake();

                            System.out.println(remake + "after");
                            stand = 0;
                            remake = 0;
                            players = new ArrayList<>();
                            temp = new ArrayList<>();
                            Thread.sleep(200);
                            sendCard(current);
                            break;
                    }

                }
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void log(String message) {
        System.out.println(message);
    }

    public ArrayList<PokerCard> createNewDesk(ArrayList<PokerCard> desk) {
        desk = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            HeartCard heart = new HeartCard(i + 1);
            ClubCard club = new ClubCard(i + 1);
            SpadesCard spade = new SpadesCard(i + 1);
            DiamondCard diamon = new DiamondCard(i + 1);
            desk.add(heart);
            desk.add(spade);
            desk.add(club);
            desk.add(diamon);
        }
        Collections.shuffle(desk);
        return desk;
    }

    public synchronized ExchangeMessage messageSendIndexs() {
        ExchangeMessage message = new ExchangeMessage();
        System.out.println("---" + index.get(0));
        message.setIndex(index.get(0));
        index.add(2);
        index.remove(0);
        System.out.println("+++" + index.get(0));
        message.setCommand(ExchangeMessage.SERVER_SEND_INDEX);
        return message;
    }

    public synchronized ExchangeMessage messageSendCards(ArrayList<PlayerInfo> playersInfo) {
        ExchangeMessage message = new ExchangeMessage();
        players = new ArrayList<>();
        for (PlayerInfo playerInfo : playersInfo) {
            players.add(playerInfo.getPlayer());
        }
        message.setPlayers(players);
        message.setCommand(ExchangeMessage.CLIENT_SEND_CARD_TO_PLAYER);
        return message;
    }

    public synchronized void setNewCardsToRemake() {
        if (remake == 2) {
            desk = createNewDesk(desk);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    temp.add(desk.remove(0));
                }
                playersInfo.get(i).getPlayer().setCards(temp);
                temp = new ArrayList<>();
            }
            System.out.println("Set xong");
        }
        remake++;
    }

    public synchronized void setNewCardsToPlayers() {
        desk = createNewDesk(desk);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                temp.add(desk.remove(0));
            }
            playersInfo.get(i).getPlayer().setCards(temp);
            temp = new ArrayList<>();
        }
        System.out.println("Set xong");
    }

    public synchronized void sendCard(PlayerInfo current) {

        try {

            OutputStream ox_sendC = current.getSocket().getOutputStream();
            ObjectOutputStream oos_sendC = new ObjectOutputStream(ox_sendC);
            oos_sendC.writeObject(messageSendCards(playersInfo));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
