package model;

import java.io.Serializable;
import java.util.*;

public class GameState implements Serializable {
    private Map<String, Player> players = new LinkedHashMap<>();
    private String currentTurn;
    private static final int BOARD_SIZE = 52; // Main circular path
    private static final int HOME_STRETCH = 5; // Home column length
    private static final int WINNING_POSITION = BOARD_SIZE + HOME_STRETCH;
    
    public void addPlayer(Player p) {
        players.put(p.getName(), p);
        // Assign colors in order: Red(0), Green(1), Yellow(2), Blue(3)
        p.setColor(players.size() - 1);
        if (currentTurn == null) currentTurn = p.getName();
    }
    
    public synchronized String movePlayer(String playerName, int dice) {
        Player player = players.get(playerName);
        if (player == null || player.hasFinished()) return "Invalid move";
        
        // If token is in home, need 6 to start
        if (player.isInHome()) {
            if (dice == 6) {
                player.moveToStart();
                return playerName + " rolled 6! Token enters the board. Roll again!";
            } else {
                nextTurn();
                return playerName + " rolled " + dice + ". Need 6 to start!";
            }
        }
        
        // Calculate new position
        int currentPos = player.getPosition();
        int newPos = currentPos + dice;
        
        // Check if entering home stretch
        int homeStretchStart = getHomeStretchStart(player.getColor());
        
        if (currentPos < homeStretchStart && newPos >= homeStretchStart) {
            // Entering home stretch
            int stepsIntoHome = newPos - homeStretchStart;
            if (stepsIntoHome > HOME_STRETCH) {
                // Can't move, need exact number
                nextTurn();
                return playerName + " rolled " + dice + ". Need exact number to finish!";
            }
            newPos = BOARD_SIZE + stepsIntoHome;
        } else if (currentPos >= BOARD_SIZE) {
            // Already in home stretch
            if (newPos >= WINNING_POSITION) {
                // Can't move, need exact number
                nextTurn();
                return playerName + " rolled " + dice + ". Need exact number to finish!";
            }
        } else {
            // Normal movement on main path
            if (newPos >= BOARD_SIZE) {
                newPos = newPos % BOARD_SIZE;
            }
        }
        
        // Check if player reached finish
        if (newPos == WINNING_POSITION - 1) {
            player.setFinished(true);
            player.setPosition(newPos);
            nextTurn();
            return playerName + " reached the finish! 🎉";
        }
        
        // Check for capture
        String capturedPlayer = checkCapture(newPos, playerName);
        
        player.setPosition(newPos);
        
        String result = playerName + " moved " + dice + " steps";
        if (capturedPlayer != null) {
            result += " and captured " + capturedPlayer + "!";
        }
        
        // Extra turn if rolled 6
        if (dice != 6) {
            nextTurn();
        } else {
            result += " Roll again!";
        }
        
        return result;
    }
    
    private int getHomeStretchStart(int color) {
        // Position where player enters home column
        switch(color) {
            case 0: return 51; // Red enters home after position 51
            case 1: return 12; // Green enters home after position 12
            case 2: return 25; // Yellow enters home after position 25
            case 3: return 38; // Blue enters home after position 38
            default: return 0;
        }
    }
    
    private String checkCapture(int position, String currentPlayer) {
        // Don't capture on safe zones
        if (isSafeZone(position)) return null;
        
        // Don't capture in home stretch
        if (position >= BOARD_SIZE) return null;
        
        // Check if another player is on this position
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            String name = entry.getKey();
            Player p = entry.getValue();
            
            if (!name.equals(currentPlayer) && p.getPosition() == position && !p.isInHome()) {
                p.sendHome();
                return name;
            }
        }
        return null;
    }
    
    private boolean isSafeZone(int position) {
        // Starting positions are safe
        return position == 0 || position == 13 || position == 26 || position == 39 ||
               // Star positions (every 13th square) are safe
               position == 8 || position == 21 || position == 34 || position == 47;
    }
    
    private void nextTurn() {
        List<String> keys = new ArrayList<>(players.keySet());
        int currentIndex = keys.indexOf(currentTurn);
        
        // Find next player who hasn't finished
        for (int i = 1; i <= keys.size(); i++) {
            int nextIndex = (currentIndex + i) % keys.size();
            String nextPlayer = keys.get(nextIndex);
            if (!players.get(nextPlayer).hasFinished()) {
                currentTurn = nextPlayer;
                return;
            }
        }
    }
    
    public Map<String, Player> getPlayers() { return players; }
    public String getCurrentTurn() { return currentTurn; }
}