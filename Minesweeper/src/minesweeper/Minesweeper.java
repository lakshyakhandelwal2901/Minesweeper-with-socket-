package minesweeper;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Minesweeper extends JFrame {

    private JLabel statusbar;

    public Minesweeper() {
        initUI();
    }

    private void initUI() {

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        Board board = new Board(statusbar);
        add(board);

        // Add Key Listener to Show Leaderboard on 'L' Press
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    String leaderboard = LeaderboardClient.getLeaderboard();
                    JOptionPane.showMessageDialog(null, leaderboard, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        setFocusable(true); // Ensure key listener works
        setResizable(false);
        pack();

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var ex = new Minesweeper();
            ex.setVisible(true);
        });
    }
}
