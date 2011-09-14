package dk.jlh.games.lines;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import dk.jlh.games.lines.Board.Space;

public class LinesView extends SurfaceView implements SurfaceHolder.Callback {

    // w: 320, h: 405
    int width = 0;
    int height = 0;
    Lines controller;
    Bitmap[] bitmaps;

    int boardXCoord = 0;
    int boardYCoord = 0;
    int gap = 1;
    float cellWidth = (width - gap) / 9; // ints cast to float
    float borderWidth = (int) (width - gap - (cellWidth * 9)) / 2;

    float lastTouchX = 0F;
    float lastTouchY = 0F;
    float lastTouchSize = 0F;
    private Board board;
    private SurfaceHolder holder;

    public LinesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        controller.startWorker();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // FIXME
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        controller.setRunning(false);
        while (retry) {
            try {
                controller.joinWorker();
                retry = false;
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }

    protected void doDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint black = new Paint();
        black.setColor(0x80000000);

        canvas.drawRect(boardXCoord(0), boardYCoord(0), boardXCoord(9) + gap, boardYCoord(9) + gap, black);

        Paint txtPaint = new Paint();
        txtPaint.setColor(0xff000000);
        txtPaint.setTextSize(30f);

        Paint selectedColour = new Paint();
        selectedColour.setColor(0xff2020f0);
        Paint bgPaint = new Paint();
        bgPaint.setColor(0xff8090a0);

        for (int i = 0; i < 10; i++) {
            canvas.drawLine(boardXCoord(0), boardYCoord(i), boardXCoord(9) + gap, boardYCoord(i), bgPaint);
            canvas.drawLine(boardXCoord(i), boardYCoord(0), boardXCoord(i), boardYCoord(9) + gap, bgPaint);
        }

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Space space = board.getSpace(x, y);
                if (space.selected) {
                    canvas.drawRect(boardXCoord(x) + 1, boardYCoord(y) + 1, boardXCoord(x + 1), boardYCoord(y + 1),
                            selectedColour);
                } else {
                    Paint bgColour = new Paint();
                    bgColour.setColor(0xff00ff00 + (0x00111100 - 0x00000011) * Math.min(space.distanceToDest, 15));
                    canvas.drawRect(boardXCoord(x) + 1, boardYCoord(y) + 1, boardXCoord(x + 1), boardYCoord(y + 1),
                            bgColour);
                }
                if (space.occupant > 0) {
                    canvas.drawBitmap(bitmaps[0] /* FIXME */, boardXCoord(x), boardYCoord(y), bgPaint);
                    canvas.drawText("" + space.occupant, boardXCoord(x) + 5, boardYCoord(y) + cellWidth - 5, txtPaint);
                }
            }
        }

        // DEBUG
//        Paint blue = new Paint();
//        blue.setColor(0xff0000ff);
//        for (Space space : board.getFreeSet()) {
//            canvas.drawCircle(boardXCoord(space.getX()) + 3, boardYCoord(space.getY()) + 3, 3, blue);
//        }

        // DEBUG
        canvas.drawCircle(lastTouchX, lastTouchY, lastTouchSize + 5, bgPaint);
    }

    float boardXCoord(int x) {
        return boardXCoord + borderWidth + x * cellWidth;
    }

    float boardYCoord(int y) {
        return boardYCoord + borderWidth + y * cellWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        cellWidth = (width - gap) / 9; // ints cast to float
        borderWidth = (int) (width - gap - (cellWidth * 9)) / 2;

        setMinimumWidth(width);
        setMinimumHeight(height);

        Log.d("Lines", "Screensize:(" + width + "," + height + ").");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // DEBUG
        lastTouchX = event.getX();
        lastTouchY = event.getY();
        lastTouchSize = event.getSize();

        // int touchSize = (int) event.getSize();
        int boardX = (int) ((event.getX() - boardXCoord) / cellWidth);
        int boardY = (int) ((event.getY() - boardYCoord) / cellWidth);
        return boardX >= 0 && boardX < 9 && boardY >= 0 && boardY < 9 && controller.onTouchEvent(boardX, boardY);
    }

    public void setController(Lines controller) {
        this.controller = controller;
    }

    public void setBitmaps(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public SurfaceHolder getTheHolder() {
        return holder;
    }
}
