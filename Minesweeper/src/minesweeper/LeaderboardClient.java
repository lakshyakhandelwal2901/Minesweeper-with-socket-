package minesweeper;

import java.io.*;
import java.net.*;
import java.util.List;

import javax.swing.JOptionPane;

public class LeaderboardClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 5000;

    public static void submitScore(String playerName, int score) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeObject("SUBMIT_SCORE");
            output.writeObject(playerName);
            output.writeInt(score);
            output.flush();

            String response = (String) input.readObject();
            System.out.println(response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getLeaderboard() {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeObject("GET_LEADERBOARD");
            output.flush();	

            List<?> leaderboard = (List<?>) input.readObject();

            StringBuilder leaderboardText = new StringBuilder("Leaderboard:\n");
            for (Object obj : leaderboard) {
                leaderboardText.append(obj).append("\n");
            }

            return leaderboardText.toString(); // ✅ Return formatted leaderboard text

        } catch (IOException | ClassNotFoundException e) {
            return "Error: Unable to retrieve leaderboard.";
        }
    }
}