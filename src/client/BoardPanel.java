package client;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BoardPanel extends JPanel {
    private Map<String, Integer> positions;
    private int[] colors;
    
    private static final Color RED = new Color(239, 68, 68);
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color YELLOW = new Color(250, 204, 21);
    private static final Color BLUE = new Color(59, 130, 246);
    
    private static final Color RED_LIGHT = new Color(254, 202, 202);
    private static final Color GREEN_LIGHT = new Color(187, 247, 208);
    private static final Color YELLOW_LIGHT = new Color(254, 249, 195);
    private static final Color BLUE_LIGHT = new Color(191, 219, 254);
    
    private static final Color PATH_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(0, 0, 0);
    private static final Color STAR_COLOR = new Color(255, 215, 0);
    
    private static final int GRID_SIZE = 15;
    private static final int CELL_SIZE = 36;
    
    private Point[] pathCoordinates = new Point[52];
    
    public BoardPanel() {
        setBackground(new Color(240, 240, 245));
        setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE + 40, GRID_SIZE * CELL_SIZE + 40));
        initializePathCoordinates();
    }
    
    private void initializePathCoordinates() {
        int idx = 0;
        
        // RED PATH - Starting from position 0 (row 6, col 1) going RIGHT
        pathCoordinates[idx++] = new Point(1, 6);  // Position 0 - RED START
        pathCoordinates[idx++] = new Point(2, 6);
        pathCoordinates[idx++] = new Point(3, 6);
        pathCoordinates[idx++] = new Point(4, 6);
        pathCoordinates[idx++] = new Point(5, 6);
        pathCoordinates[idx++] = new Point(6, 6);  // Position 5
        
        // Going UP the left column
        pathCoordinates[idx++] = new Point(6, 5);
        pathCoordinates[idx++] = new Point(6, 4);
        pathCoordinates[idx++] = new Point(6, 3);  // Position 8 - STAR (safe)
        pathCoordinates[idx++] = new Point(6, 2);
        pathCoordinates[idx++] = new Point(6, 1);
        pathCoordinates[idx++] = new Point(6, 0);  // Position 11
        
        // Turn RIGHT at top
        pathCoordinates[idx++] = new Point(7, 0);
        
        // GREEN PATH - Starting from position 13 (row 0, col 8) going DOWN
        pathCoordinates[idx++] = new Point(8, 0);  // Position 13 - GREEN START
        pathCoordinates[idx++] = new Point(8, 1);
        pathCoordinates[idx++] = new Point(8, 2);
        pathCoordinates[idx++] = new Point(8, 3);
        pathCoordinates[idx++] = new Point(8, 4);
        pathCoordinates[idx++] = new Point(8, 5);
        pathCoordinates[idx++] = new Point(8, 6);  // Position 19
        
        // Going RIGHT on top middle row
        pathCoordinates[idx++] = new Point(9, 6);
        pathCoordinates[idx++] = new Point(10, 6); // Position 21 - STAR (safe)
        pathCoordinates[idx++] = new Point(11, 6);
        pathCoordinates[idx++] = new Point(12, 6);
        pathCoordinates[idx++] = new Point(13, 6);
        pathCoordinates[idx++] = new Point(14, 6); // Position 25
        
        // Turn DOWN at right side
        pathCoordinates[idx++] = new Point(14, 7);
        
        // YELLOW PATH - Starting from position 27 (row 8, col 14) going LEFT
        pathCoordinates[idx++] = new Point(14, 8); // Position 27 - YELLOW START  
        pathCoordinates[idx++] = new Point(13, 8);
        pathCoordinates[idx++] = new Point(12, 8);
        pathCoordinates[idx++] = new Point(11, 8);
        pathCoordinates[idx++] = new Point(10, 8);
        pathCoordinates[idx++] = new Point(9, 8);
        pathCoordinates[idx++] = new Point(8, 8);  // Position 33
        
        // Going LEFT on bottom middle row
        pathCoordinates[idx++] = new Point(7, 8);
        pathCoordinates[idx++] = new Point(6, 8);  // Position 35 - STAR (safe)
        pathCoordinates[idx++] = new Point(5, 8);
        pathCoordinates[idx++] = new Point(4, 8);
        pathCoordinates[idx++] = new Point(3, 8);  // Position 38
        
        // BLUE PATH - Starting from position 39 (row 8, col 2) going UP
        pathCoordinates[idx++] = new Point(2, 8);  // Position 39 - BLUE START
        pathCoordinates[idx++] = new Point(2, 9);
        pathCoordinates[idx++] = new Point(2, 10);
        pathCoordinates[idx++] = new Point(2, 11);
        pathCoordinates[idx++] = new Point(2, 12);
        pathCoordinates[idx++] = new Point(2, 13);
        pathCoordinates[idx++] = new Point(2, 14); // Position 45
        
        // Going UP on right column
        pathCoordinates[idx++] = new Point(1, 14);
        pathCoordinates[idx++] = new Point(1, 13); // Position 47 - STAR (safe)
        pathCoordinates[idx++] = new Point(1, 12);
        pathCoordinates[idx++] = new Point(1, 11);
        pathCoordinates[idx++] = new Point(1, 10);
        pathCoordinates[idx++] = new Point(1, 9);  // Position 51
    }
    
    public void updatePositions(Map<String, Integer> pos, int[] playerColors) {
        this.positions = pos;
        this.colors = playerColors;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int boardSize = GRID_SIZE * CELL_SIZE;
        int offsetX = (getWidth() - boardSize) / 2;
        int offsetY = (getHeight() - boardSize) / 2;
        
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillRoundRect(offsetX + 3, offsetY + 3, boardSize, boardSize, 15, 15);
        
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(offsetX, offsetY, boardSize, boardSize, 15, 15);
        
        drawHomeArea(g2d, offsetX, offsetY, RED_LIGHT, RED);
        drawHomeArea(g2d, offsetX + 9 * CELL_SIZE, offsetY, GREEN_LIGHT, GREEN);
        drawHomeArea(g2d, offsetX, offsetY + 9 * CELL_SIZE, YELLOW_LIGHT, YELLOW);
        drawHomeArea(g2d, offsetX + 9 * CELL_SIZE, offsetY + 9 * CELL_SIZE, BLUE_LIGHT, BLUE);
        
        drawPaths(g2d, offsetX, offsetY);
        drawHomeTriangles(g2d, offsetX, offsetY);
        drawCenterHome(g2d, offsetX + boardSize / 2, offsetY + boardSize / 2);
        drawGridLines(g2d, offsetX, offsetY, boardSize);
        
        if (positions != null) {
            drawTokens(g2d, offsetX, offsetY);
        }
    }
    
    private void drawHomeArea(Graphics2D g2d, int x, int y, Color lightColor, Color darkColor) {
        g2d.setColor(lightColor);
        g2d.fillRect(x, y, 6 * CELL_SIZE, 6 * CELL_SIZE);
        
        g2d.setColor(darkColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x, y, 6 * CELL_SIZE, 6 * CELL_SIZE);
        
        int centerX = x + 3 * CELL_SIZE;
        int centerY = y + 3 * CELL_SIZE;
        int circleSize = (int)(CELL_SIZE * 2.5);
        
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(centerX - circleSize/2, centerY - circleSize/2, circleSize, circleSize);
        g2d.setColor(darkColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(centerX - circleSize/2, centerY - circleSize/2, circleSize, circleSize);
    }
    
    private void drawPaths(Graphics2D g2d, int offsetX, int offsetY) {
        for (int i = 0; i < 52; i++) {
            Point p = pathCoordinates[i];
            int x = offsetX + p.x * CELL_SIZE;
            int y = offsetY + p.y * CELL_SIZE;
            
            g2d.setColor(PATH_COLOR);
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            
            if (i == 0) {
                drawStartMarker(g2d, x, y, RED, "START");
            } else if (i == 13) {
                drawStartMarker(g2d, x, y, GREEN, "START");
            } else if (i == 27) {
                drawStartMarker(g2d, x, y, YELLOW, "START");
            } else if (i == 39) {
                drawStartMarker(g2d, x, y, BLUE, "START");
            }
            
            if (i == 8 || i == 21 || i == 35 || i == 47) {
                drawStar(g2d, x + CELL_SIZE/2, y + CELL_SIZE/2, 8);
            }
        }
    }
    
    private void drawStartMarker(Graphics2D g2d, int x, int y, Color color, String text) {
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));
        g2d.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
        
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
        
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 8));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (CELL_SIZE - fm.stringWidth(text)) / 2;
        int textY = y + (CELL_SIZE + fm.getAscent()) / 2 - 2;
        g2d.drawString(text, textX, textY);
    }
    
    private void drawHomeTriangles(Graphics2D g2d, int offsetX, int offsetY) {
        for (int i = 0; i < 5; i++) {
            g2d.setColor(RED);
            g2d.fillRect(offsetX + (1 + i) * CELL_SIZE, offsetY + 7 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        for (int i = 0; i < 5; i++) {
            g2d.setColor(GREEN);
            g2d.fillRect(offsetX + 7 * CELL_SIZE, offsetY + (1 + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        for (int i = 0; i < 5; i++) {
            g2d.setColor(YELLOW);
            g2d.fillRect(offsetX + (9 + i) * CELL_SIZE, offsetY + 7 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        for (int i = 0; i < 5; i++) {
            g2d.setColor(BLUE);
            g2d.fillRect(offsetX + 7 * CELL_SIZE, offsetY + (9 + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }
    
    private void drawCenterHome(Graphics2D g2d, int centerX, int centerY) {
        int size = (int)(CELL_SIZE * 1.5);
        
        int[] yellowX = {centerX - size, centerX, centerX};
        int[] yellowY = {centerY, centerY, centerY + size};
        g2d.setColor(YELLOW);
        g2d.fillPolygon(yellowX, yellowY, 3);
        
        int[] greenX = {centerX, centerX + size, centerX};
        int[] greenY = {centerY - size, centerY, centerY};
        g2d.setColor(GREEN);
        g2d.fillPolygon(greenX, greenY, 3);
        
        int[] blueX = {centerX, centerX + size, centerX};
        int[] blueY = {centerY, centerY, centerY + size};
        g2d.setColor(BLUE);
        g2d.fillPolygon(blueX, blueY, 3);
        
        int[] redX = {centerX - size, centerX, centerX};
        int[] redY = {centerY, centerY, centerY - size};
        g2d.setColor(RED);
        g2d.fillPolygon(redX, redY, 3);
        
        int circleSize = (int)(size * 0.6);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - circleSize/2, centerY - circleSize/2, circleSize, circleSize);
        
        drawStar(g2d, centerX, centerY, circleSize/3);
        
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(centerX - circleSize/2, centerY - circleSize/2, circleSize, circleSize);
    }
    
    private void drawGridLines(Graphics2D g2d, int offsetX, int offsetY, int boardSize) {
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1));
        
        for (int i = 0; i <= GRID_SIZE; i++) {
            int x = offsetX + i * CELL_SIZE;
            g2d.drawLine(x, offsetY, x, offsetY + boardSize);
        }
        
        for (int i = 0; i <= GRID_SIZE; i++) {
            int y = offsetY + i * CELL_SIZE;
            g2d.drawLine(offsetX, y, offsetX + boardSize, y);
        }
        
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(offsetX, offsetY, boardSize, boardSize, 15, 15);
    }
    
    private void drawTokens(Graphics2D g2d, int offsetX, int offsetY) {
        Color[] playerColors = {RED, GREEN, YELLOW, BLUE};
        
        int index = 0;
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            int pos = entry.getValue();
            Color color = playerColors[colors != null && index < colors.length ? colors[index] : index % 4];
            
            if (pos == -1) {
                drawTokenInHome(g2d, offsetX, offsetY, color, entry.getKey(), colors != null ? colors[index] : index % 4);
            } else if (pos < 52) {
                Point p = pathCoordinates[pos];
                int x = offsetX + p.x * CELL_SIZE;
                int y = offsetY + p.y * CELL_SIZE;
                drawToken(g2d, x, y, color, entry.getKey());
            } else {
                drawTokenInHomeStretch(g2d, offsetX, offsetY, color, entry.getKey(), pos, colors != null ? colors[index] : index % 4);
            }
            
            index++;
        }
    }
    
    private void drawTokenInHome(Graphics2D g2d, int offsetX, int offsetY, Color color, String label, int playerColor) {
        int homeX = 0, homeY = 0;
        
        switch(playerColor) {
            case 0: homeX = offsetX + 3 * CELL_SIZE; homeY = offsetY + 3 * CELL_SIZE; break;
            case 1: homeX = offsetX + 12 * CELL_SIZE; homeY = offsetY + 3 * CELL_SIZE; break;
            case 2: homeX = offsetX + 3 * CELL_SIZE; homeY = offsetY + 12 * CELL_SIZE; break;
            case 3: homeX = offsetX + 12 * CELL_SIZE; homeY = offsetY + 12 * CELL_SIZE; break;
        }
        
        drawToken(g2d, homeX, homeY, color, label);
    }
    
    private void drawTokenInHomeStretch(Graphics2D g2d, int offsetX, int offsetY, Color color, String label, int pos, int playerColor) {
        int homePos = pos - 52;
        int x = 0, y = 0;
        
        switch(playerColor) {
            case 0: // Red
                x = offsetX + (1 + homePos) * CELL_SIZE;
                y = offsetY + 7 * CELL_SIZE;
                break;
            case 1: // Green
                x = offsetX + 7 * CELL_SIZE;
                y = offsetY + (1 + homePos) * CELL_SIZE;
                break;
            case 2: // Yellow
                x = offsetX + (13 - homePos) * CELL_SIZE;
                y = offsetY + 7 * CELL_SIZE;
                break;
            case 3: // Blue
                x = offsetX + 7 * CELL_SIZE;
                y = offsetY + (13 - homePos) * CELL_SIZE;
                break;
        }
        
        drawToken(g2d, x, y, color, label);
    }
    
    private void drawToken(Graphics2D g2d, int x, int y, Color color, String label) {
        int tokenSize = CELL_SIZE - 8;
        int centerX = x + CELL_SIZE / 2 - tokenSize / 2;
        int centerY = y + CELL_SIZE / 2 - tokenSize / 2;
        
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillOval(centerX + 2, centerY + 2, tokenSize, tokenSize);
        
        g2d.setColor(color);
        g2d.fillOval(centerX, centerY, tokenSize, tokenSize);
        
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(centerX, centerY, tokenSize, tokenSize);
        
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fillOval(centerX + tokenSize/4, centerY + tokenSize/4, tokenSize/3, tokenSize/3);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        String initial = label.substring(0, Math.min(1, label.length())).toUpperCase();
        int textX = centerX + (tokenSize - fm.stringWidth(initial)) / 2;
        int textY = centerY + (tokenSize + fm.getAscent()) / 2 - 2;
        g2d.drawString(initial, textX, textY);
    }
    
    private void drawStar(Graphics2D g2d, int centerX, int centerY, int radius) {
        int points = 5;
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        int innerRadius = radius / 2;
        
        for (int i = 0; i < points * 2; i++) {
            double angle = Math.PI / 2 + (i * Math.PI / points);
            int r = (i % 2 == 0) ? radius : innerRadius;
            xPoints[i] = (int) (centerX + r * Math.cos(angle));
            yPoints[i] = (int) (centerY - r * Math.sin(angle));
        }
        
        g2d.setColor(STAR_COLOR);
        g2d.fillPolygon(xPoints, yPoints, points * 2);
        g2d.setColor(STAR_COLOR.darker());
        g2d.setStroke(new BasicStroke(1));
        g2d.drawPolygon(xPoints, yPoints, points * 2);
    }
}