package mx.unison.threads;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.logging.Logger;

public class BallAnimator implements Runnable {

    public static final Logger LOG = Logger.getLogger(BallAnimator.class.getName());
    public static final int MAX_X = 640;
    public static final int MAX_Y = 480;
    public static final int SIGN = -1;

    public static final int DX = 10;
    public static final int DY = 10;
    private Ellipse2D.Double ball;

    private double ballX;
    private double ballY;

    public BallAnimator(Shape shape) {
        ball = ( Ellipse2D.Double) shape;
        ballX = ball.x;
        ballY = ball.y;
        //ball.x = ballX ;
        //ball.y = ballY;
    }

    @Override
    public void run() {
        int sY = 1;
        int sX = 1;

        while( true ) {

            double ballWidth = ball.width;
            double ballHeight = ball.height;

            if( ballY < 0 ) {
                sY = sY * SIGN;
            }

            if( ballX < 0 ) {
                sX = sX * SIGN;
            }
            if( ballY > (MAX_Y - ballHeight) ) {
                sY = sY * SIGN;
            }

            if( ballX > (MAX_X - ballWidth) ) {
                sX = sX * SIGN;
            }
            ballX = ballX + (DX * sX) ;
            ballY = ballY + (DY * sY);
            ball.x = ballX ;
            ball.y = ballY;
            LOG.info( "Ball X: " + ballX + " Y: " + ballY );
            try {
                Thread.sleep(150L);
            } catch (InterruptedException e) {
               LOG.severe(e.getMessage());
            }
        }

    }
}
