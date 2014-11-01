package uk.ac.diamond.status;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private TextView[][] items;
    private int rows;
    private int cols;

    public GridAdapter(Context c, int rows, int cols) {
        mContext = c;
        this.rows = rows;
        this.cols = cols;
        items = new TextView[rows][cols];
    }

    public void setItems(TextView[][] items) {
        this.items = items;
    }

    public void setItem(TextView item, int row, int col) {
        this.items[row][col] = item;
    }

    public int getCount() {
        return items.length * cols;
    }

    public Object getItem(int position) {
        return items[position / cols][position % cols];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return items[position / cols][position % cols];
    }
}
