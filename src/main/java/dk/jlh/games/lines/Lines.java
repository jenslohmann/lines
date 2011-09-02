package dk.jlh.games.lines;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class Lines extends Activity {

    private GameState state;
    private Board board;
    private int score;
    private Board.Space selectedSpace;
    private Random randomizer = new Random();
    private LinesView linesView;
    private Board.Space movingPieceSpace;
    private static final String TAG = "Lines";
    private int[] nextPieces = newPieces();
    private Bitmap[] bitmaps;

    /**
     * Is called solely from worker thread.
     * Moves a piece one space at a time for animation if a piece is set to move.
     */
    void movePiece() {
        if (getState() == GameState.BUSY) {
            Board.Space movingPieceSpace = getMovingPieceSpace();
            Board.Space neighbour = null;
            if (neighbour == null && movingPieceSpace.getX() > 0) {
                neighbour = board.getSpace(movingPieceSpace.getX() - 1, movingPieceSpace.getY());
                if (neighbour.distanceToDest == movingPieceSpace.distanceToDest - 1) {
                    board.setSpace(neighbour.getX(), neighbour.getY(), movingPieceSpace.occupant);
                    board.freeSpace(movingPieceSpace);
                } else {
                    neighbour = null;
                }
            }
            if (neighbour == null && movingPieceSpace.getX() < 8) {
                neighbour = board.getSpace(movingPieceSpace.getX() + 1, movingPieceSpace.getY());
                if (neighbour.distanceToDest == movingPieceSpace.distanceToDest - 1) {
                    board.setSpace(neighbour.getX(), neighbour.getY(), movingPieceSpace.occupant);
                    board.freeSpace(movingPieceSpace);
                } else {
                    neighbour = null;
                }
            }
            if (neighbour == null && movingPieceSpace.getY() > 0) {
                neighbour = board.getSpace(movingPieceSpace.getX(), movingPieceSpace.getY() - 1);
                if (neighbour.distanceToDest == movingPieceSpace.distanceToDest - 1) {
                    board.setSpace(neighbour.getX(), neighbour.getY(), movingPieceSpace.occupant);
                    board.freeSpace(movingPieceSpace);
                } else {
                    neighbour = null;
                }
            }
            if (neighbour == null && movingPieceSpace.getY() < 8) {
                neighbour = board.getSpace(movingPieceSpace.getX(), movingPieceSpace.getY() + 1);
                if (neighbour.distanceToDest == movingPieceSpace.distanceToDest - 1) {
                    board.setSpace(neighbour.getX(), neighbour.getY(), movingPieceSpace.occupant);
                    board.freeSpace(movingPieceSpace);
                } else {
                    neighbour = null;
                }
            }
            if (neighbour != null) {
                if (neighbour.distanceToDest == 0) {
                    int score = board.removeCreatedLine(neighbour);
                    Log.d(TAG, "Score:"+score);
                    setMovingPieceSpace(null);
                    if (score > 0) {
                        addToScore(score);
                    } else {
                        addToScore(board.addPieces(nextPieces));
                        setNextPieces(newPieces());
                    }

                    // FIXME Check for game over
                    setState(GameState.READY);
                } else {
                    setMovingPieceSpace(neighbour);
                }
            }
        }
    }

    private void setNextPieces(int[] pieceIds) {
        // TODO use given piece ids
        nextPieces = pieceIds;
        // TODO Combine with setScore etc..
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImageView)findViewById(R.id.nextPiece1)).setImageBitmap(bitmaps[0]);
                ((ImageView)findViewById(R.id.nextPiece2)).setImageBitmap(bitmaps[0]);
                ((ImageView)findViewById(R.id.nextPiece3)).setImageBitmap(bitmaps[0]);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        board = new Board();
        board.setController(this);

        // Read images
        bitmaps = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.item1)};

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
                            selectedSpace.selected = false;
                            movingPieceSpace = selectedSpace;
                            selectedSpace = null;
                            setState(GameState.BUSY);
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

    private void addToScore(int increment) {
        setScore(score + increment);
    }

    private void setScore(final int score) {
        this.score = score;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.score)).setText("" + score);
            }
        });
    }

    public GameState getState() {
        return state;
    }

    void setState(GameState state) {
        this.state = state;

        Log.d(TAG, "State " + state.name());
        // FIXME Not implemented yet
    }

    public Board.Space getMovingPieceSpace() {
        return movingPieceSpace;
    }

    public void setMovingPieceSpace(Board.Space movingPieceSpace) {
        this.movingPieceSpace = movingPieceSpace;
    }
}