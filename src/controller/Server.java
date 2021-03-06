package controller;

import controller.ConnectionHandler;
import teamgarbo.github.com.badbotapp.message.InitialMessage;
import teamgarbo.github.com.badbotapp.message.Message;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ArrayList<Socket> connectedPlayers = new ArrayList<>();

    private HashMap<String, ConnectionHandler> userID_connection = new HashMap<>();
    private HashMap<ConnectionHandler, String> connection_userID = new HashMap<>();

    public Server() {
        new Thread() {
            public void run() {
                init();
            }
        }.start();
    }

    public void init() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Server: started!");

            while (true) {
                Socket client = serverSocket.accept();
                connectedPlayers.add(client);
                executorService.execute(new ConnectionHandler(client, this));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void passMessage(Message message, ConnectionHandler handler) {
        if (message instanceof InitialMessage) {
            userID_connection.put(message.getPlayerID(), handler);
            connection_userID.put(handler, message.getPlayerID());
        }

        Controller.getInstance().processMessage(message);
    }

    public void sendMessage(String userID, Message message) {
        userID_connection.get(userID).sendMessage(message);
    }

    public void closeSocket(String userID) {
        System.out.println("Server closing socket for " + userID);
        userID_connection.get(userID).closeSocket();
    }
}
