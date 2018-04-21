package com.example.idpproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Law Kin Ping on 25/3/2018.
 */

public class Record extends Fragment {
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;
    String test;
    ListView listview;
    View root;

    String userName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_record,container,false);
        listview = (ListView) root.findViewById(R.id.listViewRecord);
        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(getActivity());
        userName = getArguments().getString("userName");
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Records");
        // Extract Information from database after the view is settled
        DoExtractRecord doExtractRecord=new DoExtractRecord();
        doExtractRecord.execute();
    }
    private class DoExtractRecord extends AsyncTask<String,String,String> {

        boolean isSuccess=false;

        // 建立一個 2D arrayList 裝data
        ArrayList<ArrayList<String>> record = new ArrayList<ArrayList<String>>();
        // index for 有幾多行data
        int i = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String z="";
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Please check your internet connection";
                } else {

                    String query="select carParkName, startDateTime, endDateTime, Fee from booking where userName='"+userName+"' ";
                    Statement stmt = con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    record.add(new ArrayList<String>());


                    while (rs.next()) {
                        // array[0] 係 carParkName，先從get左 parkingSpaceID，再係carPark table 度搵翻個名

                        record.get(i).add(rs.getString(1));

                        // array[1] 係startDateTime , array[2] 係endDateTime, array[3] 係duration, array[4] 係fee
                        record.get(i).add(rs.getString(2));
                        record.get(i).add(rs.getString(3));
                        record.get(i).add(rs.getString(4));
                        //record.get(i).add(rs.getString(7));
                        i++;
                        record.add(new ArrayList<String>());
                    }

                    isSuccess = true;
                }
            }
            catch (Exception ex)
            {
                z = "Exceptions"+ex;
            }
            return z;

        }
        // 做一個 list 既 mapping <Key, Data>
        // 呢度個Key 係 Name, Distance, Vacancy
        @Override
        protected void onPostExecute(String s) {


            List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            Map<String, Object> title = new HashMap<String, Object>();

            // 第一行: 標題
            title.put("Name", "CarPark");
            title.put("Start", "Start");
            title.put("End", "End");
            //title.put("Duration", "Duration");
            title.put("Fee", "Fee");
            items.add(title);

            // 第N行: data
            for (int i = 0; i < record.size() - 1; i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("Name", record.get(i).get(0));
                item.put("Start", record.get(i).get(1));
                item.put("End", record.get(i).get(2));
                //item.put("Duration", record.get(i).get(3));
                item.put("Fee", record.get(i).get(3));
                items.add(item);
            }

            // Load data 入去listview
            SimpleAdapter adapter = new SimpleAdapter(
                    getActivity(),
                    items,
                    R.layout.style_listviewrecord,
                    new String[]{"Name", "Start", "End", "Fee"},
                    new int[]{R.id.tvCarParkName, R.id.tvStart, R.id.tvEnd, R.id.tvFee}
            ) {
            };


            listview.setAdapter(adapter);
             /*
            AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment fragment = new ParkingSpaceSelection();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.cotent_main,fragment).commit();
                }
            };
            listview.setOnItemClickListener(onClickListView);
        */
        }

    }
}
