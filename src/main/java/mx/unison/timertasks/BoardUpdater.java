package mx.unison.timertasks;

import java.util.TimerTask;

public class BoardUpdater extends TimerTask {
    private Board board;

    private boolean isPaused = false;

    public BoardUpdater(Board board) {
        this.board = board;
    }

    @Override
    public void run() {
        if (!isPaused) {
            board.repaint();
        }
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }


    public void pause() {
        this.isPaused = true;
    }

    public void resume() {
        this.isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

}
