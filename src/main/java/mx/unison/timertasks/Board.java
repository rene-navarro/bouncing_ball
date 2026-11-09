package mx.unison.timertasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.logging.Logger;
import java.util.Timer;



public class Board extends JComponent
        implements ActionListener, KeyListener  {

    private static final Logger LOG = Logger.getLogger(Board.class.getName());

    private Dimension preferredSize = null;  // dimsensiones del tablero
    private Ellipse2D.Double ball;   // pelota que rebota

    private Timer ballAnimator;  // contrala el movimiento de la pelota
    private Timer boardRefresher;  // controla cada cuando se refresca el tablero

    private BallAnimator ballModel;  // modelo de la pelota

    private boolean paused = false;  // indica si el tablero esta pausado o no

    public Board() {
        super();

        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));

        // dimensiones y posicion iniciales de la pelota
        ball = new Ellipse2D.Double(320,240,20,20);

        ballModel = new BallAnimator(ball);
        ballAnimator = new Timer("BounceThread");
        ballAnimator.schedule(ballModel,  150);

        boardRefresher = new Timer("BoardRefresher");
        boardRefresher.schedule(new BoardUpdater(this), 100);

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // pintar fondo
        if (isOpaque()) {
            g.setColor( getBackground() );
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(getForeground());
        }

        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(5.0f));

        g2.setColor(Color.RED);

        // pintar la pelota en el tablero
        g2.fill(ball);
        g2.setColor(Color.BLACK);
    }

    public Dimension getPreferredSize() {
        if (preferredSize == null) {
            return new Dimension(640, 480);
        } else {
            return super.getPreferredSize();
        }
    }

    public void setPreferredSize(Dimension newPrefSize) {
        preferredSize = newPrefSize;
        super.setPreferredSize(newPrefSize);
    }

    public void zoomIn() {
        if(ball.width > 100) {
            return;
        }
        ball.width = ball.width * 1.1;
        ball.height = ball.height * 1.1;
    }

    public void zoomOut() {
        if(ball.width < 20) {
            return;
        }
        ball.width = ball.width * 0.9;
        ball.height = ball.height * 0.9;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println( e.getKeyChar() );
        char key = e.getKeyChar();
        if( key == 'O' || key == 'o' ) {
            LOG.info("ZOOM_OUT");
            zoomOut();
        }
        if( key == 'I' || key == 'i' ) {
            LOG.info("ZOOM_IN");
            zoomIn();
        }
        if( key == 'P' || key == 'p' ) {
            LOG.info("PAUSE");
            pause();
        }
        if( key == 'R' || key == 'r' ) {
            LOG.info("RESUME");
            resume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void pause() {
        paused = true;

    }

    public void resume() {
        paused = false;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
