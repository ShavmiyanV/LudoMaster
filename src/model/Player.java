package model;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int position = -1;  // -1 means token is in home (not on board yet)
    private boolean hasFinished = false;
    private int color; // 0=Red, 1=Green, 2=Yellow, 3=Blue
    
    public Player(String name) { 
        this.name = name; 
    }
    
    public String getName() { return name; }
    public int getPosition() { return position; }
    public boolean hasFinished() { return hasFinished; }
    public int getColor() { return color; }
    
    public void setPosition(int position) { 
        this.position = position;
    }
    
    public void setFinished(boolean finished) {
        this.hasFinished = finished;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
    
    public boolean isInHome() {
        return position == -1;
    }
    
    public void moveToStart() {
        // Starting positions based on color
        switch(color) {
            case 0: position = 0; break;   // Red starts at position 0
            case 1: position = 13; break;  // Green starts at position 13
            case 2: position = 26; break;  // Yellow starts at position 26
            case 3: position = 39; break;  // Blue starts at position 39
        }
    }
    
    public void sendHome() {
        position = -1;
    }
}