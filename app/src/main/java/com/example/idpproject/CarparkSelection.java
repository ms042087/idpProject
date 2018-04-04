package com.example.idpproject;

import android.app.ProgressDialog;
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

import android.widget.Toast;

// Database
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Law Kin Ping on 25/3/2018.
 * Back end job completed on 4/4/2018
 */
public class CarparkSelection extends Fragment {

    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    TextView carPark1Name, carPark1Distance, carPark1Vacancy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.activity_carpark_selection,container,false);
        carPark1Name = (TextView) root.findViewById(R.id.carPark1Name);
        carPark1Distance = (TextView) root.findViewById(R.id.carPark1Distance);
        carPark1Vacancy = (TextView) root.findViewById(R.id.carPark1Vacancy);
        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(getActivity());
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Carpark Selection");
        // Extract Information from database after the view is settled
        DoExtractCarParkInfo doExtractCarParkInfo=new DoExtractCarParkInfo();
        doExtractCarParkInfo.execute();
    }

    private class DoExtractCarParkInfo extends AsyncTask<String,String,String> {

        boolean isSuccess=false;
        String vacancy;

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
                        String query="select position from carpark where parkingSpaceID=1";
                        Statement stmt = con.createStatement();
                        ResultSet rs=stmt.executeQuery(query);
                        while (rs.next()) {
                            vacancy = rs.getString(1);
                            Toast.makeText(getActivity(), String.valueOf(vacancy), Toast.LENGTH_SHORT).show();
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

        @Override
        protected void onPostExecute(String s) {
            if (Integer.parseInt(vacancy) > 0) {
                carPark1Name.setText("ABC Carpark");
                carPark1Name.setTextColor(Color.BLUE);
                carPark1Distance.setText("1.2 km");
                carPark1Vacancy.setText(String.valueOf(vacancy));
            } else
                carPark1Vacancy.setText("No vacancy");

            carPark1Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new ParkingSpaceSelection();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.cotent_main,fragment).commit();
                }
            });
           // progressDialog.hide();
        }

    }

}
