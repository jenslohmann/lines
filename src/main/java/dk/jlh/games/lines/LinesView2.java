package dk.jlh.games.lines;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class LinesView2 extends View {
    Controller cntrlr;

    // w: 320, h: 430

    public LinesView2(Context context, Controller cntrlr) {
        super(context);
        this.cntrlr = cntrlr;

        ArrayList<View> views = new ArrayList<View>();
        GridView gridView = new GridView(context);
        gridView.setNumColumns(9);
        gridView.setColumnWidth(30);
        gridView.setAdapter(new GridListAdapter(context));
        views.add(gridView);
        addTouchables(views);
    }
    
    class GridListAdapter extends BaseAdapter {

        Context context;
        
        public GridListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 81;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
//            return null;
            return new Button(context);

        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button button = new Button(context);
            button.setWidth(30);
            button.setHeight(30);
            button.setText("A");
            return button;
        }
    }
}
