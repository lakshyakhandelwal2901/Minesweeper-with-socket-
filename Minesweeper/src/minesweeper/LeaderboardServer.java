package minesweeper;

import java.io.*;
import java.net.*;
import java.util.*;

public class LeaderboardServer {
    private static final int PORT = 5000; // Server listens on this port
    private static final int MAX_LEADERBOARD_SIZE = 10; // Store top 10 scores
    private List<PlayerScore> leaderboard = new ArrayList<>();

    public static void main(String[] args) {
        LeaderboardServer server = new LeaderboardServer();
        server.startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Leaderboard Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

                String request = (String) input.readObject();
                
                if (request.equals("SUBMIT_SCORE")) {
                    String playerName = (String) input.readObject();
                    int score = input.readInt();
                    addScore(playerName, score);
                    output.writeObject("Score submitted successfully!");
                } else if (request.equals("GET_LEADERBOARD")) {
                    output.writeObject(getLeaderboard());
                }

                output.flush();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void addScore(String playerName, int score) {
        leaderboard.add(new PlayerScore(playerName, score));
        leaderboard.sort(Comparator.comparingInt(PlayerScore::getScore).reversed());

        if (leaderboard.size() > MAX_LEADERBOARD_SIZE) {
            leaderboard.remove(leaderboard.size() - 1);
        }
    }

    private synchronized List<PlayerScore> getLeaderboard() {
        return new ArrayList<>(leaderboard);
    }

    // PlayerScore class to store player data
    private static class PlayerScore implements Serializable {
        private final String name;
        private final int score;

        public PlayerScore(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return name + " - " + score;
        }
    }
}
