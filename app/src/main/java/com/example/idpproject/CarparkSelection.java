package com.example.idpproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


// Database
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

// ListView
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Law Kin Ping on 25/3/2018.
 * Load data successfully on 4/4/2018
 * Load data into ListView on 5/4/2018
 */
public class CarparkSelection extends Fragment {

    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    ListView listview;
    View root;

    String checkIfBooked;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.activity_carpark_selection,container,false);
        listview = (ListView) root.findViewById(R.id.listView);
        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(getActivity());
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Carpark Selection");
        DoExtractCarParkInfo doExtractCarParkInfo = new DoExtractCarParkInfo();
        doExtractCarParkInfo.execute();

    }

    private class DoExtractCarParkInfo extends AsyncTask<String,String,String> {

        boolean isSuccess=false;

        // 建立一個 2D arrayList 裝data
        ArrayList<ArrayList<String>> carParkInfo = new ArrayList<ArrayList<String>>();
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
                        String query="select carParkName,vacancy from carpark";
                        Statement stmt = con.createStatement();
                        ResultSet rs=stmt.executeQuery(query);
                        carParkInfo.add(new ArrayList<String>());

                        // 逐行data拎，第1個 column 係id，唔需要，第2個係 Name，第3個係vacancy
                        // array[0] 係 Name (data第2個)，array[1]係 Distance，老作，array[2]係vacancy (data第3個)
                        while (rs.next()) {
                            carParkInfo.get(i).add(rs.getString(1));
                            carParkInfo.get(i).add("1 km");
                            carParkInfo.get(i).add(rs.getString(2));
                            i++;
                            carParkInfo.add(new ArrayList<String>());
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
            List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
            Map<String, Object> title = new HashMap<String, Object>();

            // 第一行: 標題
            title.put("Name", "Name");
            title.put("Distance", "Distance");
            title.put("Vacancy", "Vacancy");
            items.add(title);

            // 第N行: data
            for (int i=0;i < carParkInfo.size()-1;i++){
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("Name", carParkInfo.get(i).get(0));
                item.put("Distance", carParkInfo.get(i).get(1));
                item.put("Vacancy", carParkInfo.get(i).get(2));
                items.add(item);
            }

            // Load data 入去listview
            SimpleAdapter adapter = new SimpleAdapter(
                    getActivity(),
                    items,
                    R.layout.style_listview,
                    new String[]{"Name", "Distance", "Vacancy"},
                    new int[]{R.id.tvName, R.id.tvDistance, R.id.tvVacancy}
            ){};

            listview.setAdapter(adapter);

            //
             AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment fragment = new ParkingSpaceSelection();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main_1,fragment).commit();
                }
            };
            listview.setOnItemClickListener(onClickListView);

        }

    }

}
