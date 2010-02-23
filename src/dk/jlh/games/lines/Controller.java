package dk.jlh.games.lines;

import java.util.Random;

import dk.jlh.games.lines.Board.Space;
import android.view.View;

public class Controller {
    private Board board;
    int score;

    /** Accepts user input. */
    static final int READY = 1;
    /** Does not accept user input - is occupied. */
    static final int GOING = 2;
    /** Accepts restricted input (such as "New game"). Game is over. */
    static final int GAME_OVER = 3;

    int state = GAME_OVER;
    private View view;
    private Space selectedSpace;
    private Random randomizer = new Random();

    public Controller() {
        board = new Board();

        initialize();
    }

    void initialize() {
        setState(GOING);
        board.addPieces(newPieces());
        score = 0;
        setState(READY);
    }

    private void addToScore(int increment) {
        score += increment;
        // TODO Update GUI w/ new score.

    }

    void setState(int state) {
        this.state = state;

        // FIXME Not implemented yet
    }

    public Board getBoard() {
        return board;
    }

    public void setView(View view) {
        this.view = view;
    }

    boolean onTouchEvent(int x, int y, int size) {
        if (state == READY) {
            Space space = board.getSpace(x, y);
            if (space.occupant > 0) {
                if (selectedSpace != null) {
                    selectedSpace.selected = false;
                }
                space.selected = true;
                selectedSpace = space;
            } else {
                if (selectedSpace != null && selectedSpace != space) {
                    // Moving a piece
                    board.calcDistances(selectedSpace, space);
                    if (selectedSpace.distanceToDest < 100) {
                        // FIXME Must move
                        int occupant = selectedSpace.occupant;
                        board.freeSpace(selectedSpace);
                        selectedSpace.selected = false;
                        selectedSpace = null;
                        board.setSpace(x, y, occupant);

                        // When moving has ended
                        int score;
                        if ((score = board.removeCreatedLine(space)) != 0) {
                            addToScore(score);
                        } else {
                            board.addPieces(newPieces());
                        }
                    }
                }
            }
        }

        view.invalidate();
        return false;
    }

    private int[] newPieces() {
        return new int[] { 1 + randomizer.nextInt(5), 1 + randomizer.nextInt(5), 1 + randomizer.nextInt(5) };
    }
}
