package com.example.karup.clockspot;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by karup on 1/14/2017.
 */


public class GridviewAdapter extends BaseAdapter {
    private ArrayList<String> activities;
    private ArrayList<Integer> imageId;
    private Activity activity;

    public GridviewAdapter(Activity activity, ArrayList<String> activities,
                           ArrayList<Integer> imageId) {
        super();
        this.activities = activities;
        this.imageId = imageId;
        this.activity = activity;
    }

    public int getCount() {
        return activities.size();
    }

    public String getItem(int position) {
        return activities.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    public static class ViewHolder {
        //The position of this row in list
        private int position;

        //The image view for each row
        private ImageView imageView;

        //The textView for each row
        private TextView textView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflator = activity.getLayoutInflater();
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.gridview_row, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.textView = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        holder.imageView.setImageResource(imageId.get(position));
        holder.textView.setText(activities.get(position));


        return convertView;
    }


}






