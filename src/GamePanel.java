import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import resources.Constants;

public class GamePanel extends JPanel implements ActionListener {

    //constants
    private final int screenWidth = Constants.SCREEN_WIDTH;
    private final int screenHeight = Constants.SCREEN_HEIGHT;
    private final int delay = Constants.DELAY;
    private final int units = Constants.UNITS;
    private final int squareSize = Constants.SQUARE_SIZE;

    //attributes
    private int fruitsEaten = 0;
    private int size = Constants.INITIAL_SIZE;
    private Point fruit;
    private char direction = 'R';
    boolean running = false;
    private final int[] x = new int[units];
    private final int[] y = new int[units];
    private Timer timer;
    private Random random = new Random();

    public GamePanel(){
        Dimension windowSize = new Dimension(screenWidth, screenHeight);
        this.setPreferredSize(windowSize);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        generateFruit();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(fruit.x, fruit.y, squareSize, squareSize);

            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], squareSize, squareSize);
                } else {
                    g.setColor(new Color(40, 175, 0));
                    g.fillRect(x[i], y[i], squareSize, squareSize);

                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD,30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + fruitsEaten,
                    (screenWidth - metrics.stringWidth("Score: " + fruitsEaten))/5 * 4  ,g.getFont().getSize() );

        }
        else {
            gameOver(g);
        }
    }

    public void generateFruit(){
        fruit = new Point(random.nextInt((int)screenWidth/squareSize) * squareSize,
                random.nextInt((int)screenHeight/squareSize) * squareSize);
    }
    public void move(){
        for(int i=size; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - squareSize;
                break;
            case 'D':
                y[0] = y[0] + squareSize;
                break;
            case 'L':
                x[0] = x[0] - squareSize;
                break;
            case 'R':
                x[0] = x[0] + squareSize;
                break;
        }
    }

    public void checkFruit(){

        if(x[0] == fruit.x && y[0] == fruit.y){
            size++;
            fruitsEaten++;
            generateFruit();

        }
    }

    public void checkCollisions(){
        for(int i=size; i>0; i--){
            if(x[0] == x[i] && y[0] == y[i]){
                running = false;
            }

            if(x[0] < 0){
                x[0] = screenWidth;
            }

            if(x[0] > screenWidth){
                x[0] = 0;
            }

            if(y[0] > screenWidth){
                y[0] =0;
            }
            if(y[0] <0){
                y[0] = screenWidth;
            }

            if(!running){
                timer.stop();
            }
        }
    }

    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (screenWidth - metrics.stringWidth("GAME OVER"))/2,screenHeight/2 );

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + fruitsEaten, (screenWidth - metrics1.stringWidth("Score: " + fruitsEaten))/2,g.getFont().getSize() );

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkFruit();
            checkCollisions();
        }
        repaint();
    }
}
