package com.example.karup.clockspot;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karup on 1/30/2017.
 */

public class ProgressFragment extends Fragment {
    private ArrayList<String> timer = new ArrayList<>();
    private ArrayList<String> activityList = new ArrayList<>();
    private ArrayList<String> activityList1 = new ArrayList<>();
    DatabaseHelperActivity helper;
    TotalTimer activity;
    PieChart pieChart;
    String uname;
    String secondsString;
    String[] seconds;
    int seco;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//                setHasOptionsMenu(true);
        TotalTimer activity = (TotalTimer) getActivity();
        View rootView = inflater.inflate(R.layout.activity_progress, container, false);
        helper = new DatabaseHelperActivity(getActivity());
        uname = activity.getMyData();

        Log.d("TotalTimer", "**On Create View Username** " + uname);
        activityList = helper.getActivitiesName(uname);
        timer = helper.getTimer(uname);

       for(int i = 0; i< activityList.size();i++)
       {
           secondsString = timer.get(i);
           seconds = secondsString.split(":");
           seco = Integer.parseInt(seconds[0]) * 3600 + Integer.parseInt(seconds[1]) * 60 + Integer.parseInt(seconds[2]) * 1;
           if (seco != 0) {
               activityList1.add(activityList.get(i));
           }
       }



        pieChart = (PieChart) rootView.findViewById(R.id.pieChart);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setEntryLabelTextSize(8);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Activities");
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setCenterTextSize(14);
        pieChart.setDrawEntryLabels(true);
        pieChart.setNoDataText("No data to show Pie Chart");
        pieChart.invalidate();
        pieChart.setNoDataTextColor(Color.WHITE);
        pieChart.setHighlightPerTapEnabled(true);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.WHITE);
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setXEntrySpace(7);
        legend.setYEntrySpace(5);

        try {
            Boolean dataPresent = addDataSet();
            if (!dataPresent) {
                pieChart.setCenterText("No data to show Pie Chart");
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning!!")
                        .setMessage("No data to show Pie Chart")
                        .setPositiveButton("Ok", null)
                        .create().show();
            }


        } catch (Exception e) {
            Log.v("ProgressView", "Exception Caught" + e.getMessage());
            // Toast.makeText(getApplicationContext(),"Exception caught",Toast.LENGTH_SHORT).show();
        }


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                try {
                    String detailedTimer = "";
                    String[] detailedTimerArray;
                    // Toast.makeText(getContext(),h.toString(),Toast.LENGTH_SHORT).show();
                    int pos = e.toString().lastIndexOf("y");
                    String runs = e.toString().substring(pos + 3);


                    for (int i = 0; i < timer.size(); i++) {
                        secondsString = timer.get(i);
                        seconds = secondsString.split(":");
                        Float sec = Float.parseFloat(seconds[0]) * 3600 + Float.parseFloat(seconds[1]) * 60 + Float.parseFloat(seconds[2]) * 1;

                        float y = Float.parseFloat(String.valueOf(sec));
                        if (y == Float.parseFloat(runs)) {
                            pos = i;
                            break;
                        }
                    }

                    int positionActivity = h.toString().indexOf("x:");
                    String position = h.toString().substring(positionActivity + 3, positionActivity + 4);
                    String ActivityName = activityList1.get(Integer.parseInt(position));
                    // Toast.makeText(getContext(),h.toString().substring(positionActivity+3,positionActivity+4),Toast.LENGTH_SHORT).show();


                    detailedTimer = "";
                    detailedTimerArray = timer.get(pos).split(":");

                    if (detailedTimerArray[0].equals("00")) {
                        detailedTimer += "";
                        if (detailedTimerArray[1].equals("00")) {
                            detailedTimer += "";
                        } else {
                            detailedTimer += Integer.parseInt(detailedTimerArray[1]) + "min, ";
                        }
                        detailedTimer += Integer.parseInt(detailedTimerArray[2]) + "s";

                    } else {
                        detailedTimer += Integer.parseInt(detailedTimerArray[0]) + "h, " + Integer.parseInt(detailedTimerArray[1]) + "min, "
                                + Integer.parseInt(detailedTimerArray[2]) + "s";
                    }


                    Toast.makeText(getActivity(), "Activity Name: " + ActivityName + " " + detailedTimer, Toast.LENGTH_SHORT).show();
                }
                catch (Exception exception)
                {
                    Log.e("PROGRESS","Exception caught"+exception.getMessage());
                    Toast.makeText(getActivity(), "Could not retireve data, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected() {

            }

        });


        return rootView;
    }

    private Boolean addDataSet() {

        Boolean dataNotEmpty = false;

        List<PieEntry> entries = new ArrayList<>();


        for (int i = 0; i < timer.size(); i++) {
            secondsString = timer.get(i);
            seconds = secondsString.split(":");
            seco = Integer.parseInt(seconds[0]) * 3600 + Integer.parseInt(seconds[1]) * 60 + Integer.parseInt(seconds[2]) * 1;
            if (seco != 0) {
                entries.add(new PieEntry(seco, activityList.get(i)));
                dataNotEmpty = true;
            }

        }


        PieDataSet set = new PieDataSet(entries, "Activities Time");
        PieData data = new PieData(set);
        set.setValueTextSize(8f);
        set.notifyDataSetChanged();
        set.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh

        //Colors
        ArrayList<Integer> colors = new ArrayList<>();

        //colors.add(getResources().getColor(R.color.AlabamaCrimson));

        colors.add(getResources().getColor(R.color.red1));
        colors.add(getResources().getColor(R.color.red2));
        colors.add(getResources().getColor(R.color.red3));
        colors.add(getResources().getColor(R.color.red4));
        colors.add(getResources().getColor(R.color.red5));
        colors.add(getResources().getColor(R.color.red6));
        colors.add(getResources().getColor(R.color.red7));
        colors.add(getResources().getColor(R.color.red8));
        colors.add(getResources().getColor(R.color.red9));
        colors.add(getResources().getColor(R.color.red10));
        colors.add(getResources().getColor(R.color.AmaranthPink));
        colors.add(getResources().getColor(R.color.Amazonite));
        colors.add(getResources().getColor(R.color.Amethyst));
        colors.add(getResources().getColor(R.color.AtomicTangerine));
        colors.add(getResources().getColor(R.color.Beaver));
        colors.add(getResources().getColor(R.color.BrilliantLavender));
        colors.add(getResources().getColor(R.color.CandyPink));
        colors.add(Color.CYAN);
        colors.add(Color.GREEN);
        colors.add(Color.MAGENTA);
        colors.add(Color.RED);
        colors.add(Color.GRAY);
        //colors.add(Color.TRANSPARENT);



        set.setColors(colors);
        return dataNotEmpty;
    }

}


