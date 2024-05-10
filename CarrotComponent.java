/***
 * Displays two bunnies and randomly spawned carrots. The carrots randomly move, and the bunnies move depending on the keys the players press.
 * If the x and y coordinates of a bunny match the position of a carrot, that bunny gets a point. This program displays the bunny scores at the top of the screen.
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
/*
 * Implements the main component for the carrot game.
 */
public class CarrotComponent extends JComponent {
    public static final int SIZE = 500; // initial size
    public static final int PIXELS = 50; // square size per image
    public static final int MOVE = 20; // keyboard move
    public static final int GRAVITY = 2; // gravity move
    public static final int CARROTS = 20; // number of carrots
    private ArrayList myPoints; // x,y upper left of each carrot
    private int myX; // upper left of head x
    private int myY; // upper left of head y
    private int myDy; // change in y for gravity
    private int points;
    private Image carrotImage;
    private Image headImage;
    private int bunny1Score = 0;
    private int secondX;
    private int secondY;
    private int secondDy;
    private Image secondHeadImage;
    private int bunny2Score = 0;
    public CarrotComponent() {
        setPreferredSize(new Dimension(SIZE, SIZE));
        // getScaledInstance() gives us re-sized version of the image --
        // speeds up the drawImage() if the image is already the right size
        // See paintComponent()

        headImage = readImage("bunny.jpg");
        headImage = headImage.getScaledInstance(PIXELS, PIXELS, Image.SCALE_SMOOTH);
        //adds second bunny
        secondHeadImage = readImage("bunny2.jpg");
        secondHeadImage = secondHeadImage.getScaledInstance(PIXELS, PIXELS, Image.SCALE_SMOOTH);

        carrotImage = readImage("carrot.gif");
        carrotImage = carrotImage.getScaledInstance(PIXELS, PIXELS, Image.SCALE_SMOOTH);
        myPoints = new ArrayList();
    }

    // Utility -- create a random point within the window
    // leaving PIXELS space at the right and bottom
    private Point randomPoint() {
        Point p = new Point( (int) (Math.random() * (getWidth() - PIXELS)), (int) (Math.random() * (getHeight() - PIXELS)));
        return(p);
    }

    // Reset things for the start of a game
    public void reset() {
        myPoints.clear(); // removes all the points
        for (int i=0; i < CARROTS; i++) {
            myPoints.add( randomPoint());
        }
        myX = getWidth() / 2;
        myY = 0;
        myDy = 0;

        secondX = getWidth() /2;
        secondY = 0;
        secondDy = 0;
        repaint();
    }

    // Advance things by one tick -- do gravity, check collisions
    public void tick() {
        myDy = myDy + GRAVITY; // increase dy
        myY += myDy; // figure new y

        secondDy = secondDy + GRAVITY;
        secondY += secondDy;
        // check if hit bottom
        if (myY + PIXELS >= getHeight()) {
            // back y up
            myY -= myDy;
            // reverse direction of dy (i.e. bounce), but with 98% efficiency
            myDy = (int) (0.98 * -myDy);
        }
        if(secondY + PIXELS >= getHeight()) {
            secondY -= secondDy;
            secondDy = (int) (0.98 * -secondDy);
        }
        checkCollisions();
        repaint();
    }

    // Check the current x,y vs. the carrots
    public void checkCollisions() {
        int carrotPoints = 5;
        for (int i=0; i < myPoints.size(); i++) {
            Point point = (Point) myPoints.get(i);
            // if we overlap a carrot, remove it
            if (Math.abs(point.getX() - myX) <= PIXELS
                    && Math.abs(point.getY() - myY) <= PIXELS) {
                bunny1Score += carrotPoints;
                myPoints.remove(i); // removes the ith elem from an ArrayList
                i--; // tricky:
                // back i up, since we removed the ith element
                repaint();
            }
            if(Math.abs(point.getX() - secondX) <= PIXELS && Math.abs(point.getY() - secondY) <= PIXELS){
                bunny2Score += carrotPoints;
                myPoints.remove(i);
                i--;
                repaint();
            }
        }
        if (myPoints.size() == 0) {
            reset(); // new game
            bunny1Score = 0;
            bunny2Score = 0;
        }
    }

    //stores Bunny1 score
    public int getBunny1Score(){
        return bunny1Score;
    }

    //stores Bunny2 Score
    public int getBunny2Score(){
        return bunny2Score;
    }

    // Process one key click -- up, down, left, right
    public void key(int code) {
        if (code == KeyEvent.VK_UP) {
            myY += -MOVE;
        }
        else if (code == KeyEvent.VK_DOWN) {
            myY += MOVE;
        }
        else if (code == KeyEvent.VK_LEFT) {
            myX += -MOVE;
        }
        else if (code == KeyEvent.VK_RIGHT) {
            myX += MOVE;
        }
        else if(code == KeyEvent.VK_W){
            secondY+= -MOVE;
        }
        else if(code == KeyEvent.VK_S){
            secondY += MOVE;
        }
        else if(code == KeyEvent.VK_A){
            secondX += -MOVE;
        }
        else if(code == KeyEvent.VK_D){
            secondX += MOVE;
        }
        checkCollisions();
        repaint();
    }

    // Utility to read in an Image object
    // If image cannot load, prints error output and returns null.
    private Image readImage(String filename) {
        Image image = null;
        try {
            image = ImageIO.read(new File(filename));
        }
        catch (IOException e) {
            System.out.println("Failed to load image '" + filename + "'");
            e.printStackTrace();
        }
        return(image);
    }

    // Draws the head and carrots
    public void paintComponent(Graphics g) {
        int width = getWidth() / 2;
        g.drawString("Bunny 1 Score", 0, getHeight() /18);
        g.drawString(String.valueOf(getBunny1Score()), 100, getHeight() /18);

        g.drawString("Bunny 2 Score", width, getHeight()/18);
        g.drawString(String.valueOf(getBunny2Score()), width + 100, getHeight()/18);

        g.drawImage(headImage, myX, myY, PIXELS, PIXELS, null);
        g.drawImage(secondHeadImage, secondX, secondY, PIXELS, PIXELS, null);

        // Draw all the carrots
        for (int i = 0; i < myPoints.size(); i++) {
            Point p = (Point) myPoints.get(i);
            // point.getX() returns a double, so we must cast to int
            g.drawImage(carrotImage, (int) (p.getX()), (int) (p.getY()), PIXELS, PIXELS, null);
        }
    }
}