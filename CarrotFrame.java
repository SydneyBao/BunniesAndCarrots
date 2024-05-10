/***
 * This program creates three buttons: start, faster, and slower. Depending on which button is pressed, a corresponding action occurs.
 * The carrots move faster or slower depending on which button is pressed.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/*
 * Implements the Frame for the carrot game.
 * Manages the buttons and keyboard events.
 */
class CarrotFrame extends JFrame implements KeyListener, ActionListener {
    private CarrotComponent carrot;
    private JButton start;
    private JButton fast;
    private JButton slow;
    private int delay;
    private javax.swing.Timer timer;
    public static final int DELAY = 50; // milliseconds
    public CarrotFrame() {
        setTitle("Carrot");
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        carrot = new CarrotComponent();
        content.add(carrot, BorderLayout.CENTER);
        carrot.addKeyListener(this);
        carrot.setFocusable(true);
        JPanel panel = new JPanel();
        start = new JButton("Start");
        start.addActionListener(this);
        panel.add(start);
        start.setFocusable(false);
        fast = new JButton("Faster");
        fast.addActionListener(this);
        panel.add(fast);
        fast.setFocusable(false);
        slow = new JButton("Slower");
        slow.addActionListener(this);
        panel.add(slow);
        slow.setFocusable(false);
        delay = DELAY;
        timer = new javax.swing.Timer(delay, this);
        content.add(panel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        carrot.requestFocusInWindow();
        setVisible(true);
    }

    // Handle timer and button events
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(timer)) {
            carrot.tick();
        }
        else if (e.getSource().equals(fast)) {
            delay = (int)(delay * 0.90);
            timer.setDelay(delay);
        }
        else if (e.getSource().equals(slow)) {
            delay = (int)(delay / 0.90);
            timer.setDelay(delay);
        }
        else if (e.getSource().equals(start)) {
            carrot.reset();
            timer.start();
        }
    }

    // Must implement the next three methods to implement the KeyListener
    // interface -- aka "notifications" sent by the system on various events
    // Key typed notification -- down and up
    public void keyTyped(KeyEvent e) { /* not relevant to this application */ }

    // Key pressed -- the downstroke
    public void keyPressed(KeyEvent e) {
        // use e.getCharCode() to get its character
        // use e.getKeyCode() for things like up/down arrow
        // (See the KeyEvent class)
        carrot.key(e.getKeyCode());
    }

    // Key released -- the up stroke
    public void keyReleased(KeyEvent e) { /* not relevant to this application */ }
    public static void main(String[ ] args) {
        CarrotFrame frame = new CarrotFrame();
    }
}