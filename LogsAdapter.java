package com.example.karup.clockspot;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by karup on 1/30/2017.
 */

public class LogsAdapter extends ListFragment {
    DatabaseHelperActivity helper;
    String uname;
    int resID;
    String[] detailedTimerArray;
    ArrayList<String> activityList = new ArrayList<>();
    ArrayList<String> activityTimer = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
//        listView.setDividerHeight(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TotalTimer activity = (TotalTimer) getActivity();
        uname = activity.getMyData();
        helper = new DatabaseHelperActivity(getActivity());

        activityList = helper.getActivitiesName(uname);
        activityTimer = helper.getTimer(uname);


        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < activityTimer.size(); i++) {
            resID = (getActivity().getResources().getIdentifier
                    (activityList.get(i).toLowerCase(), "drawable", getActivity().getPackageName()));

            detailedTimerArray = activityTimer.get(i).split(":");
            String detailedTimer = "";
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


            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("aName", activityList.get(i));
            hm.put("aTimer", detailedTimer);

            if (resID == 0) {
                resID = (getActivity().getResources().getIdentifier
                        ("other", "drawable", getActivity().getPackageName()));
                hm.put("aImage", Integer.toString(resID));
            } else {
                hm.put("aImage", Integer.toString(resID));
            }

            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"aName", "aTimer", "aImage"};

        // Ids of views in listview_layout
        int[] to = {R.id.activityName, R.id.totalTimer, R.id.activityImage};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList,
                R.layout.logs_listview_layout, from, to);

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}


