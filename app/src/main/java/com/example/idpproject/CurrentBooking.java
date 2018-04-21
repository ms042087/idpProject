package com.example.idpproject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CurrentBooking extends Fragment {
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    ListView listview;
    View root;
    String userName;
    String carParkName;
    String pos;
    TextView cBCarParkName,showTime;
    ImageView ivL,ivR;

    Button cancelBooking;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.activity_current_booking, container, false);
        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(getActivity());
        //userName = getArguments().getString("userName");
        cancelBooking = (Button) root.findViewById(R.id.btCancel);
        showTime = root.findViewById(R.id.textView5);
        cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to cancel the booking?")
                        .setTitle("Cancel");

                // Add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Connection con = connectionClass.CONN();
                            if (con == null) {
                            } else {
                                // get the carParkName and pos
                                String query="select carParkName, pos from currentbooking";
                                Statement stmt = con.createStatement();
                                ResultSet rs=stmt.executeQuery(query);
                                while (rs.next()) {
                                    carParkName = rs.getString(1);
                                    pos =rs.getString(2);
                                }
                                //Toast.makeText(getActivity(), pos+" "+carParkName, Toast.LENGTH_SHORT).show();
                                // delete the record
                                String query0="delete from currentbooking where userName = 'a' ";
                                Statement stmt0 = con.createStatement();
                                stmt0.executeUpdate(query0);
                                // update the record
                                String query1;
                                if(pos.equals("1"))
                                     query1="update carpark set pos1 ='N' where carParkName= 'CarParkA' ;";
                                else
                                    query1="update carpark set pos2 ='N' where carParkName= 'CarParkA' ;";
                                Statement stmt1 = con.createStatement();
                                stmt1.executeUpdate(query1);
                                String query2="update carpark set vacancy = 2 where carParkName= 'CarParkA';";
                                Statement stmt2 = con.createStatement();
                                stmt2.executeUpdate(query2);
                            }
                        }
                        catch (Exception ex){}
                        Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_SHORT).show();
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        startActivity(intent);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
        return root;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Booking");
        // Extract Information from database after the view is settled
        DoExtractCurrentBooking doExtractCurrentBooking = new DoExtractCurrentBooking();
        doExtractCurrentBooking.execute();
    }
    private class DoExtractCurrentBooking extends AsyncTask<String,String,String> {

        boolean isSuccess=false;

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
                } else {
                    String query="select carParkName, pos from currentbooking";
                    Statement stmt = con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    while (rs.next()) {
                        carParkName = rs.getString(1);
                        pos =rs.getString(2);
                    }
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
            }
            return z;

        }
        // 做一個 list 既 mapping <Key, Data>
        // 呢度個Key 係 Name, Distance, Vacancy
        @Override
        protected void onPostExecute(String s) {
            cBCarParkName = (TextView)root.findViewById(R.id.CBCarParkName);
            ivL = root.findViewById(R.id.ivL);
            ivR = root.findViewById(R.id.ivR);
            cBCarParkName.setText(carParkName);
            if(pos.equals("1"))
                ivL.setVisibility(View.VISIBLE);
            else if (pos.equals("2"))
                ivR.setVisibility(View.VISIBLE);
            String dbtime="";
            try{
                dbtime=getTime();
            }catch(Exception e){
            }
            showTime.setText("Please arrive within "+dbtime+" minutes");

        }

        private String getTime() throws Exception{
            String dbDateTimeString="";
            String currentTimeString;
            //get Current DateTime
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTimeString = sdf.format(dt);
            //get booking DateTime
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                } else {
                    // get the carParkName and pos
                    String query="select startDateTime from currentbooking";
                    Statement stmt = con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    while (rs.next()) {
                        dbDateTimeString = rs.getString(1);
                    }
                }
            }
            catch (Exception ex){}

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dbDateTime = format.parse(dbDateTimeString);
            Date currentTime = format.parse(currentTimeString);
            long difference = 15-(currentTime.getTime() - dbDateTime.getTime())/1000/60; //second
            return Long.toString(difference);
        }


    }
}
