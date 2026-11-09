package mx.unison.threads;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.logging.Logger;

public class Board  extends JComponent
        implements Runnable,         KeyListener , ComponentListener {

    private static final Logger LOG = Logger.getLogger(Board.class.getName());

    private Dimension preferredSize = null;  // dimsensiones del tablero
    private Ellipse2D.Double ball;   // pelota que rebota

    private Thread ballAnimator;  // contrala el movimiento de la pelota
    private Thread boardRefresher;  // controla cada cuando se refresca el tablero

    private BallAnimator ballModel;

    public Board() {
        super();

        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));

        // dimensiones y posicion iniciales de la pelota
        ball = new Ellipse2D.Double(320,240,20,20);

        ballModel = new BallAnimator(ball);
        ballAnimator = new Thread(ballModel,"BounceThread");
        ballAnimator.start();

        boardRefresher = new Thread( this,"RefreshThread");
        boardRefresher.start();

       // addComponentListener(this);

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

    @Override
    public void run() {
        while ( true ) {
            repaint();

            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                 LOG.warning( e.getMessage() );
            }
        }
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
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void componentResized(ComponentEvent e) {
        LOG.info("REDIM");
        Insets insets = getInsets();
        System.out.printf("%s", insets.toString());
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
