package com.example.idpproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//back end
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParkingSpaceSelection extends Fragment {
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;
    //TextView HLLCarpark ;
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_space_selection);


    }*/
    ImageButton spaceAG, spaceAR, spaceBG, spaceBR;

    View root;
    String pos1, pos2;
    int count = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.activity_parking_space_selection,container,false);
        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(getActivity());
        spaceAG = (ImageButton) root.findViewById(R.id.spaceAG);
        spaceAR = (ImageButton) root.findViewById(R.id.spaceAR);
        spaceBG = (ImageButton) root.findViewById(R.id.spaceBG);
        spaceBR = (ImageButton) root.findViewById(R.id.spaceBR);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Parking Space Selection");
        // Extract Information from database after the view is settled
        DoExtractParkingSpaceInfo doExtractParkingSpaceInfo=new DoExtractParkingSpaceInfo();
        doExtractParkingSpaceInfo.execute();
    }
    private class DoExtractParkingSpaceInfo extends AsyncTask<String,String,String> {

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
            String z ="";
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query=" select * from carpark where parkingSpaceID = 1 ";

                        Statement stmt = con.createStatement();
                        //stmt.executeUpdate(query);

                        ResultSet rs=stmt.executeQuery(query);

                        while (rs.next())

                        {
                            pos1 = rs.getString(4);
                            pos2 =rs.getString(5);

                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex;
                }
            return z;
            }

        // 做一個 list 既 mapping <Key, Data>
        // 呢度個Key 係 Name, Distance, Vacancy
        @Override
        protected void onPostExecute(String s) {
            count=0;
         //   Toast.makeText(getActivity(), "" + pos1 + pos2, Toast.LENGTH_LONG).show();
            if (pos1.equals("N")) {
                spaceAG.setVisibility(View.VISIBLE);
                count++;
                spaceAG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to select parking slot 1?")
                                .setTitle("Selection");

                        // Add the buttons
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    Connection con = connectionClass.CONN();
                                    if (con == null) {
                                    } else {
                                        String query="UPDATE carpark SET pos1 = 'B' WHERE parkingSpaceID = 1";
                                        Statement stmt = con.createStatement();
                                        stmt.executeUpdate(query);
                                        if(count==2)
                                            query="UPDATE carpark SET Vacancy = 1 WHERE parkingSpaceID = 1";
                                        else
                                            query="UPDATE carpark SET Vacancy = 0 WHERE parkingSpaceID = 1";
                                        stmt = con.createStatement();
                                        stmt.executeUpdate(query);

                                        // gernerate date
                                        java.util.Date dt = new java.util.Date();
                                        java.text.SimpleDateFormat sdf =
                                                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String currentTime = sdf.format(dt);

                                        //query="insert into currentbooking values('a','carParkA',1,'"+currentTime+"')";
                                        query="insert into currentbooking (userName,carParkName,pos,startDateTime,isActive) values('a','carParkA',1,'"+currentTime+"','T')";

                                        stmt = con.createStatement();
                                        stmt.executeUpdate(query);
                                    }
                                }
                                catch (Exception ex)
                                {
                                    isSuccess = false;
                                }

                                Fragment fragment = new CurrentBooking();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_main_1,fragment).commit();

                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            } else {
                spaceAR.setVisibility(View.VISIBLE);
                spaceAR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Not Available...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (pos2.equals("N")) {
                spaceBG.setVisibility(View.VISIBLE);
                count++;
                spaceBG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to select parking slot 10?")
                                .setTitle("Selection");

                        // Add the buttons
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                                try {
                                    Connection con = connectionClass.CONN();
                                   //// Toast.makeText(getActivity(), "xxzzzzxx", Toast.LENGTH_SHORT).show();
                                    if (con == null) {
                                      //  Toast.makeText(getActivity(), "xxzzsasasasaszzxx", Toast.LENGTH_SHORT).show();
                                    } else {
                                      ////  Toast.makeText(getActivity(), "xxxx", Toast.LENGTH_SHORT).show();
                                        String query="UPDATE carpark SET pos2 = 'B' WHERE parkingSpaceID = 1";
                                        Statement stmt = con.createStatement();
                                        stmt.executeUpdate(query);
                                        if(count==2)
                                            query="UPDATE carpark SET Vacancy = 1 WHERE parkingSpaceID = 1";
                                        else
                                            query="UPDATE carpark SET Vacancy = 0 WHERE parkingSpaceID = 1";
                                        stmt = con.createStatement();
                                        stmt.executeUpdate(query);

                                        // gernerate date
                                        java.util.Date dt = new java.util.Date();
                                        java.text.SimpleDateFormat sdf =
                                                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String currentTime = sdf.format(dt);

                                        //query="insert into currentbooking values('a','carParkA',1,'"+currentTime+"')";
                                        query="insert into currentbooking (userName,carParkName,pos,startDateTime,isActive) values('a','carParkA',2,'"+currentTime+"','T')";

                                        stmt = con.createStatement();
                                        stmt.executeUpdate(query);

                                    }
                                }
                                catch (Exception ex)
                                {
                                    isSuccess = false;
                                }

                                Fragment fragment = new CurrentBooking();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_main_1,fragment).commit();
                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            } else{
                spaceBR.setVisibility(View.VISIBLE);
                spaceBR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Not Available...", Toast.LENGTH_SHORT).show();
                    }
                });
            }



        }

    }
}
