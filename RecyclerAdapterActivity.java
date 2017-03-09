package com.example.karup.clockspot;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;


/**
 * Created by karup on 12/26/2016.
 */

public class RecyclerAdapterActivity extends RecyclerView.Adapter<RecyclerAdapterActivity.ViewHolder> {

    String uname;
    long startTime;
    long timeWhenStopped = 0;
    DatabaseHelperActivity helper;
    CursorAdapter mCursorAdapter;
    Context mContext;
    private static final String MESSAGE = "MESSAGE";
    int result;



    public RecyclerAdapterActivity(Context context, Cursor c) {

        mContext = context;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                View view = LayoutInflater.from(context).inflate(
                        R.layout.row, parent, false);
                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);



                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
                final Chronometer stopWatch = (Chronometer) view.findViewById(R.id.chrono);
                startTime = SystemClock.elapsedRealtime();
                helper = new DatabaseHelperActivity(context.getApplicationContext());
                final ViewHolder viewHolder = (ViewHolder) view.getTag();
                uname = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                final String activity = cursor.getString(cursor.getColumnIndexOrThrow("activity"));
                final String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String imageText = activity.toLowerCase();

                viewHolder.nameView.setText(activity);

                viewHolder.timerView.setText(time);


                int resID;
                Drawable d = null;
                resID = context.getResources().getIdentifier(imageText, "drawable", context.getPackageName());

                try {
                    d = context.getResources().getDrawable(resID);
                } catch (Exception e) {
                    resID = context.getResources().getIdentifier("other", "drawable", context.getPackageName());
                    d = context.getResources().getDrawable(resID);
                }

                viewHolder.imageView.setImageDrawable(d);



                viewHolder.bool = false;




                viewHolder.startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (viewHolder.bool) {
                            timeWhenStopped = stopWatch.getBase() - SystemClock.elapsedRealtime();
                            stopWatch.stop();

                            String totalTime = viewHolder.timerView.getText().toString();

                            Log.v("ItemAdapter", "Pause Button Update Timer Values " +
                                    "uname = " + uname +
                                    "time = " + totalTime +
                                    "activity = " + activity + "ok");


                            try {
                                helper.updateTimer(uname, totalTime, activity);
                            } catch (Exception e) {
                                Log.v("PAUSEBUTTON", "updateTImerException" + e.getMessage());
                            }

                            viewHolder.startButton.setText("START");
                            viewHolder.bool = false;
                        } else {

                            stopWatch.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                            Log.v("STARTBUTTON", "timewhenstopped" + timeWhenStopped);
                            stopWatch.start();
                            viewHolder.startButton.setText("PAUSE");
                            viewHolder.bool = true;
                        }

                    }


                });


                viewHolder.stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopWatch.stop();
                        String totalTime = viewHolder.timerView.getText().toString();
                        Log.v("ItemAdapter Stop Button", "Update Timer Values uname = " +
                                "" + uname + " totaltime = " + totalTime + " activity = " + activity + "");

                        try {
                            helper.updateTimer(uname, totalTime, activity);
                        } catch (Exception e) {
                            Log.v("STOPBUTTON", "updateTImerException" + e.getMessage());
                        }


                        timeWhenStopped = 0;
                        viewHolder.timerView.setText("00:00:00");
                        viewHolder.startButton.setText("START");
                        viewHolder.bool = false;

                    }
                });


                stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer arg0) {
                        timeWhenStopped = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;

                        String asText = String.format("%02d:%02d:%02d", (timeWhenStopped / 3600),
                                (timeWhenStopped / 60) % 60, (timeWhenStopped % 60));
                        viewHolder.timerView.setText(asText);
                    }
                });
            }
        };
    }



    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameView;
        public final TextView timerView;
        public final ImageView imageView;
        public final Button startButton;
        public final Button stopButton;
        Boolean bool = false;


        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.nameTextView);
            timerView = (TextView) itemView.findViewById(R.id.timerTextView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            startButton = (Button) itemView.findViewById(R.id.startButton);
            stopButton = (Button) itemView.findViewById(R.id.stopButton);


        }

    }
}






