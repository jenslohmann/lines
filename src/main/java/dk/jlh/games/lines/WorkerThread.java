package dk.jlh.games.lines;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Thread to perform the animation.
 */
class WorkerThread extends Thread {
    private LinesView linesView;
    private Lines controller;
    final SurfaceHolder surfaceHolder;

    public WorkerThread(LinesView linesView, Lines controller) {
        this.linesView = linesView;
        this.controller = controller;
        surfaceHolder = linesView.getTheHolder();
    }

    @Override
    public void run() {
        while (controller.isRunning()) {
            controller.movePiece();
            controller.removePiece();
            // FIXME Check for state change - don't redraw if stable
            synchronized (surfaceHolder) {
                Canvas canvas = surfaceHolder.lockCanvas();
                try {
                    linesView.doDraw(canvas);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
