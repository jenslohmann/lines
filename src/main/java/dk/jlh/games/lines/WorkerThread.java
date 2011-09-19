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
        Canvas prevCanvas = null;
        while (controller.isRunning()) {
            boolean dirty = controller.movePiece();
            dirty |= controller.removePiece();

            synchronized (surfaceHolder) {
                Canvas canvas = surfaceHolder.lockCanvas();
                try {
                    // FIXME Fix blinking etc.
                    // if (dirty || prevCanvas != canvas) {
                        prevCanvas = canvas;
                        linesView.doDraw(canvas);
                    // }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
