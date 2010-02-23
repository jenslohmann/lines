package dk.jlh.games.lines;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class Lines extends Activity {

    Controller cntrlr;
    private Bitmap[] bitmaps;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cntrlr = new Controller();
		cntrlr.getBoard().setCntrl(cntrlr);
		
	      // Read images
        bitmaps = new Bitmap[] { BitmapFactory.decodeResource(getResources(), R.drawable.item1) };
		
		LinesView view = new LinesView(this, cntrlr);
		cntrlr.setView(view);
		setContentView(view);
		view.invalidate();
	}

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }
}