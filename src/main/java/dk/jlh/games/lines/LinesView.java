package dk.jlh.games.lines;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import dk.jlh.games.lines.Board.Space;

public class LinesView extends View {

    // w: 320, h: 430
    int width = 0;
    int height = 0;
    Lines controller;
    Bitmap[] bitmaps;

    int boardXCoord = 0;
    int boardYCoord = 40;
    int gap = 1;
    float cellWidth = (width - gap) / 9; // ints cast to float
    float borderWidth = (int) (width - gap - (cellWidth * 9)) / 2;

    float lastTouchX = 0F;
    float lastTouchY = 0F;
    float lastTouchSize = 0F;
    private Board board;

    public LinesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint bgPaint = new Paint();
        bgPaint.setColor(0xff8090a0);
        Paint txtPaint = new Paint();
        txtPaint.setColor(0xff000000);
        txtPaint.setTextSize(35f);

        canvas.drawText("w:" + width + " h:" + height + " cw:" + cellWidth, 12, 12, bgPaint);

        //canvas.drawText("Score:" + controller.score, 100, 30, bgPaint);
        
        Paint selected = new Paint();
        selected.setColor(0xff2020f0);

        for (int i = 0; i < 10; i++) {
            canvas.drawLine(boardXCoord(0), boardYCoord(i), boardXCoord(9) + gap, boardYCoord(i), bgPaint);
            canvas.drawLine(boardXCoord(i), boardYCoord(0), boardXCoord(i), boardYCoord(9) + gap, bgPaint);
        }

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Space space = board.getSpace(x, y);
                if (space.selected) {
                    canvas.drawRect(boardXCoord(x), boardYCoord(y), boardXCoord(x + 1) - 1, boardYCoord(y + 1) - 1,
                            selected);
                }
                if (space.occupant > 0) {
                    canvas.drawBitmap(bitmaps[0] /* FIXME */, boardXCoord(x), boardYCoord(y), bgPaint);
                    canvas.drawText("" + space.occupant, boardXCoord(x) + 5, boardYCoord(y) + cellWidth - 5, txtPaint);
                }
                // DEBUG
                if(space.distanceToDest < 20) {
                    canvas.drawText("" + space.distanceToDest, boardXCoord(x) + 10, boardYCoord(y) + cellWidth - 5, bgPaint);
                }
            }
        }

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

        // FIXME Set width, height to 9 x 9 board + score etc..
        setMinimumWidth(width);
        setMinimumHeight(height);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // DEBUG
        lastTouchX = event.getX();
        lastTouchY = event.getY();
        lastTouchSize = event.getSize();

        int touchSize = (int) event.getSize();
        int boardX = (int) ((event.getX() - boardXCoord) / cellWidth);
        int boardY = (int) ((event.getY() - boardYCoord) / cellWidth);
        return boardX >= 0 && boardX < 9 && boardY >= 0 && boardY < 9 && controller.onTouchEvent(boardX, boardY, touchSize);
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
}
