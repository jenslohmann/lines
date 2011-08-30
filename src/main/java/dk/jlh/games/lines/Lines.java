package dk.jlh.games.lines;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.widget.TextView;

import java.util.Random;

public class Lines extends Activity {

    private GameState state;
    private Board board;
    private int score;
    private Board.Space selectedSpace;
    private Random randomizer = new Random();
    private LinesView linesView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        board = new Board();
        board.setController(this);

        // Read images
        Bitmap[] bitmaps = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.item1)};

        setContentView(R.layout.main);
        linesView = (LinesView) findViewById(R.id.board);
        linesView.setController(this);
        linesView.setBitmaps(bitmaps);
        linesView.setBoard(board);

        initialize();
    }

    void initialize() {
        setState(GameState.BUSY);
        board.addPieces(newPieces());
        setScore(0);
        setState(GameState.READY);
    }

    boolean onTouchEvent(int x, int y) {
        switch (state) {
            case READY:
                Board.Space space = board.getSpace(x, y);
                if (space.occupant > 0) {
                    if (selectedSpace != null) {
                        selectedSpace.selected = false;
                    }
                    space.selected = true;
                    selectedSpace = space;
                } else {
                    if (selectedSpace != null && selectedSpace != space) {
                        board.calcDistances(selectedSpace, space);
                        // Moving a piece
                        if (selectedSpace.distanceToDest < 100) {
                            // FIXME Must move using animation
                            int occupant = selectedSpace.occupant;
                            board.freeSpace(selectedSpace);
                            selectedSpace.selected = false;
                            selectedSpace = null;
                            board.setSpace(x, y, occupant);

                            state = GameState.BUSY;

                            int score = board.removeCreatedLine(space);
                            if (score > 0) {
                                addToScore(score);
                            } else {
                                addToScore(board.addPieces(newPieces()));
                            }

                            // FIXME Check for game over
                            state = GameState.READY;
                        }
                    }
                }
                break;
            case BUSY:
                break;
            case GAME_OVER:
                break;
        }

        linesView.invalidate();
        return false;
    }

    private int[] newPieces() {
        return new int[]{1 + randomizer.nextInt(5), 1 + randomizer.nextInt(5), 1 + randomizer.nextInt(5)};
    }

    void setState(GameState state) {
        this.state = state;

        // FIXME Not implemented yet
    }

    private void addToScore(int increment) {
        setScore(score + increment);
    }

    private void setScore(int score) {
        this.score = score;
        ((TextView) findViewById(R.id.score)).setText("" + score);
    }
}