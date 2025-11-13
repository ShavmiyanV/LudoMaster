package client;

import model.Message;
import model.Player;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LudoBoardUI extends JFrame {
    private JLabel infoLabel;
    private JButton rollDiceButton;
    private JButton chatToggleButton;
    private ObjectOutputStream out;
    private String playerName;
    private BoardPanel boardPanel;
    private ChatPanel chatPanel;
    private JSplitPane mainSplitPane;
    private boolean chatVisible = true;
    
    // Modern Blue Theme Colors
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color PRIMARY_HOVER = new Color(0, 105, 217);
    private static final Color DARK_BG = new Color(18, 18, 28);
    private static final Color PANEL_BG = new Color(25, 30, 48);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color ACCENT_COLOR = new Color(64, 169, 255);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);

    public LudoBoardUI(String playerName, ObjectOutputStream out) {
        this.playerName = playerName;
        this.out = out;

        setTitle("🎲 Ludo Game - " + playerName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_BG);

        initializeComponents();

        setSize(1300, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1100, 700));
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Top panel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - Split between game board and chat
        mainSplitPane = createMainSplitPane();
        add(mainSplitPane, BorderLayout.CENTER);
        
        // Bottom panel - Dice roll button
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Left side - Title
        JLabel titleLabel = new JLabel("🎲 Ludo Master");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Center - Info label
        infoLabel = new JLabel("Waiting for game to start...", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoLabel.setForeground(ACCENT_COLOR);
        panel.add(infoLabel, BorderLayout.CENTER);
        
        // Right side - Player name and chat toggle
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(PANEL_BG);
        
        JLabel playerLabel = new JLabel("Player: " + playerName);
        playerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        playerLabel.setForeground(TEXT_COLOR);
        rightPanel.add(playerLabel);
        
        // Chat toggle button
        chatToggleButton = new JButton("Close Chat");
        chatToggleButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chatToggleButton.setBackground(PRIMARY_COLOR);
        chatToggleButton.setForeground(Color.WHITE);
        chatToggleButton.setFocusPainted(false);
        chatToggleButton.setBorderPainted(false);
        chatToggleButton.setPreferredSize(new Dimension(110, 35));
        chatToggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        chatToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chatToggleButton.setBackground(PRIMARY_HOVER);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                chatToggleButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        chatToggleButton.addActionListener(e -> toggleChat());
        rightPanel.add(chatToggleButton);
        
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JSplitPane createMainSplitPane() {
        // Left side - Game board
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setBackground(DARK_BG);
        boardContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        boardPanel = new BoardPanel();
        boardContainer.add(boardPanel, BorderLayout.CENTER);
        
        // Right side - Chat panel
        chatPanel = new ChatPanel(playerName, out);
        
        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardContainer, chatPanel);
        splitPane.setDividerLocation(680);
        splitPane.setDividerSize(3);
        splitPane.setResizeWeight(0.65);
        splitPane.setBorder(null);
        splitPane.setBackground(DARK_BG);
        
        return splitPane;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(10, 20, 15, 20));
        
        // Dice roll button
        rollDiceButton = new JButton("🎲 Roll Dice");
        rollDiceButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rollDiceButton.setBackground(PRIMARY_COLOR);
        rollDiceButton.setForeground(Color.WHITE);
        rollDiceButton.setFocusPainted(false);
        rollDiceButton.setBorderPainted(false);
        rollDiceButton.setPreferredSize(new Dimension(180, 50));
        rollDiceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rollDiceButton.setEnabled(false);
        
        rollDiceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (rollDiceButton.isEnabled()) {
                    rollDiceButton.setBackground(PRIMARY_HOVER);
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rollDiceButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        rollDiceButton.addActionListener((ActionEvent e) -> rollDice());
        panel.add(rollDiceButton);
        
        return panel;
    }
    
    private void toggleChat() {
        chatVisible = !chatVisible;
        
        if (chatVisible) {
            mainSplitPane.setRightComponent(chatPanel);
            mainSplitPane.setDividerLocation(680);
            chatToggleButton.setText("Close Chat");
        } else {
            mainSplitPane.setRightComponent(null);
            mainSplitPane.setDividerLocation(1.0);
            chatToggleButton.setText("Open Chat");
        }
        
        mainSplitPane.revalidate();
        mainSplitPane.repaint();
    }

    private void rollDice() {
        try {
            out.writeObject(new Message(Message.ROLL, "", playerName));
            out.flush();
            rollDiceButton.setEnabled(false);
            showMessage("Rolling dice...");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error sending roll: " + e.getMessage());
        }
    }

    public void updatePositionsFromState(String stateText) {
        Map<String, Integer> newPos = new HashMap<>();
        int[] colors = new int[4];
        
        try {
            String[] parts = stateText.split(",");
            int idx = 0;
            for (String p : parts) {
                if (p.contains("=")) {
                    String[] kv = p.trim().split("=");
                    String name = kv[0];
                    int pos = Integer.parseInt(kv[1]);
                    newPos.put(name, pos);
                    colors[idx] = idx; // Assign colors in order
                    idx++;
                }
            }
            boardPanel.updatePositions(newPos, colors);
        } catch (Exception e) {
            System.err.println("Error parsing state: " + e.getMessage());
        }
    }

    public void showMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            infoLabel.setText(msg);
            
            // Change color based on message type
            if (msg.contains("Your turn")) {
                infoLabel.setForeground(SUCCESS_COLOR);
            } else if (msg.contains("Need 6") || msg.contains("Need exact")) {
                infoLabel.setForeground(new Color(239, 68, 68));
            } else if (msg.contains("Roll again")) {
                infoLabel.setForeground(new Color(250, 204, 21));
            } else {
                infoLabel.setForeground(ACCENT_COLOR);
            }
        });
    }

    public void enableRoll(boolean enable) {
        SwingUtilities.invokeLater(() -> {
            rollDiceButton.setEnabled(enable);
            if (enable) {
                rollDiceButton.setBackground(PRIMARY_COLOR);
            } else {
                rollDiceButton.setBackground(new Color(100, 100, 120));
            }
        });
    }
    
    public void receiveChatMessage(Message msg) {
        chatPanel.receiveMessage(msg);
    }
    
    public void updateUserList(String[] users) {
        chatPanel.updateUserList(users);
    }
    
    public void updatePlayers(Map<String, Player> players) {
        Map<String, Integer> positions = new HashMap<>();
        int[] colors = new int[players.size()];
        
        int index = 0;
        for (Player player : players.values()) {
            positions.put(player.getName(), player.getPosition());
            colors[index] = player.getColor();
            index++;
        }
        
        boardPanel.updatePositions(positions, colors);
    }
}