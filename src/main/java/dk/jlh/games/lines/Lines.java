package dk.jlh.games.lines;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lines extends Activity {

    private GameState state;
    private Board board;
    private int score;
    private Board.Space selectedSpace;
    private Random randomizer = new Random();
    private Board.Space movingPieceSpace;
    private static final String TAG = "Lines";
    private int[] nextPieces = newPieces();
    private Bitmap[] bitmaps;
    private boolean running = true;
    private Thread worker;
    private List<Board.Space> toRemove = new ArrayList<Board.Space>(10);
    private HighScoreHandler scoreHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        board = new Board();
        board.setController(this);

        // Read images
        bitmaps = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.item1)};

        setContentView(R.layout.main);
        LinesView linesView = (LinesView) findViewById(R.id.board);
        linesView.setController(this);
        linesView.setBitmaps(bitmaps);
        linesView.setBoard(board);
        worker = new WorkerThread(linesView, this);

        scoreHandler = new HighScoreHandler();

        initialize();
    }

    void initialize() {
        setState(GameState.BUSY);
        board.addPieces(nextPieces);
        setNextPieces(newPieces());
        setScore(0);
        setState(GameState.READY);
    }

    /**
     * Is called solely from worker thread.
     * Moves a piece one space at a time for animation if a piece is set to move.
     * @return boolean returns {@code true} iff a piece was actually moved.
     */
    boolean movePiece() {
        boolean dirty = false;
        if (getState() == GameState.BUSY) {
            Board.Space movingPieceSpace = getMovingPieceSpace();

            Board.Space neighbour = movingPieceSpace.getSpaceCloserToDest();
            if (neighbour != null) {
                dirty = true;
                board.setSpace(neighbour.getX(), neighbour.getY(), movingPieceSpace.occupant);
                board.freeSpace(movingPieceSpace);
                if (neighbour.distanceToDest == 0) {
                    List<Board.Space> spacesToRemove = board.removeCreatedLine(neighbour);
                    toRemove.addAll(spacesToRemove);
                    setMovingPieceSpace(null);
                    if (spacesToRemove.isEmpty()) {
                        board.addPieces(nextPieces);
                        setNextPieces(newPieces());
                    }

                    if (getState() == GameState.GAME_OVER) {
                        scoreHandler.addNewScore(score);
                    } else {
                        setState(GameState.READY);
                    }
                } else {
                    setMovingPieceSpace(neighbour);
                }
            }
            if (getState() == GameState.BUSY && toRemove.isEmpty() && movingPieceSpace == null) {
                Log.d("LINES", "YAYA!");
                setState(GameState.READY);
            }
        }
        return dirty;
    }

    private void setNextPieces(int[] pieceIds) {
        // TODO use given piece ids
        nextPieces = pieceIds;
        // TODO Combine with setScore etc..
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImageView) findViewById(R.id.nextPiece1)).setImageBitmap(bitmaps[0]);
                ((ImageView) findViewById(R.id.nextPiece2)).setImageBitmap(bitmaps[0]);
                ((ImageView) findViewById(R.id.nextPiece3)).setImageBitmap(bitmaps[0]);
            }
        });
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

        return false;
    }

    /**
     * Removes next piece in line to be removed in the animation.
     * @return boolean returns {@code true} iff a piece was removed.
     */
    public boolean removePiece() {
        if (!toRemove.isEmpty()) {
            board.freeSpace(toRemove.remove(0));
            addToScore(1);
            return true;
        }
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
    }

    public Board.Space getMovingPieceSpace() {
        return movingPieceSpace;
    }

    public void setMovingPieceSpace(Board.Space movingPieceSpace) {
        this.movingPieceSpace = movingPieceSpace;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void startWorker() {
        worker.start();
    }

    public void joinWorker() throws InterruptedException {
        worker.join();
    }
}