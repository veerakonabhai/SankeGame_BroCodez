import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    // size for the game box
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    // unit size for the object
    static final int UNIT_SIZE = 25;
    //dividing to get total number of grids
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / (UNIT_SIZE*UNIT_SIZE);
    // delay time
    static final int DELAY = 75;
    // 2 arrays to store the information of the snake body and it cannot cross
    // GAME_UNITS/  respective window units size
    final int[] x = new int[GAME_UNITS/(SCREEN_WIDTH/UNIT_SIZE)];
    final int[] y = new int[GAME_UNITS/(SCREEN_HEIGHT/UNIT_SIZE)];
    // base size to start with
    int bodyParts = 6;
    int applesEaten;
    // coordinates of apple to appear;
    int appleX;
    int appleY;
    // R - right, L - left, U - up, D - down
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            /* removed to not show grid
            // to add grid lines across x-axis and y-axis(might remove later)
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            } */
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // for loop to iterate through and draw snake body parts
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // to draw current score
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        // generate coordinates of new apple
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {// to move the snake
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        // to check if snake head overlaps with apple
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // checks if head collides with left border
        if ((x[0] < 0)) {
            running = false;
        }
        // checks if head collides with right border
        if ((x[0] > SCREEN_WIDTH)) {
            running = false;
        }
        // checks if head collides with top border
        if ((y[0] < 0)) {
            running = false;
        }
        // checks if head collides with bottom border
        if ((y[0] > SCREEN_HEIGHT)) {
            running = false;
        }

        // if any hit happens we stop timer(similar to game over)
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // score text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - scoreMetrics.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());
        // game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics gameOverMetrics = getFontMetrics(g.getFont());
        // below calculation keeps the text in most middle of box
        g.drawString("Game Over", (SCREEN_WIDTH - gameOverMetrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;

            }
        }
    }

}