package mx.unison.timertasks;

import java.util.TimerTask;

public class BoardUpdater extends TimerTask {
    private Board board;

    public BoardUpdater(Board board) {
        this.board = board;
    }

    @Override
    public void run() {
        board.repaint();
    }
}
