package com.example.idpproject;
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ABHI on 9/20/2016.
 * Modified by Ho Yu Hin on 21/3/2018
 */

public class ConnectionClass {
    String classs = "com.mysql.jdbc.Driver";

    /**
     * You can only change this
     */
    //http://idpparking.ddns.net 192.168.0.102 //
    //String url = "jdbc:mysql://192.168.0.102/mydb";
    String url = "jdbc:mysql://idpparking.ddns.net/mydb";
    String user = "idpparkingadmin";
    String password = "idpparkingadmin";
    /**
     * You can only change this
     */


    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            conn = DriverManager.getConnection(url, user, password);
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }
}
