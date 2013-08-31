package com.cwardcode.TranTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Part of: ${package_name}
 * User: Chris Ward
 * Date: 7/24/13
 * Time: 12:22 AM
 */
public class LocReceiver extends BroadcastReceiver {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://direct.cwardcode.com:3306/cdward4_Tracker";
    private static final String DB_USER = "cdward4_tracker";
    private static final String DB_PASSWORD = "tracker";

    public void onReceive(Context context, Intent intent) {
        int vehicleID =  intent.getIntExtra("VehicleID", -1);
        Double latitude = intent.getDoubleExtra("latitude", -1);
        Double longitude = intent.getDoubleExtra("longitude", -1);
        Double speed = intent.getDoubleExtra("speed", -1);
        updateRemote(vehicleID, latitude, longitude, speed);
    }
    private class SendLocationData extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... strings){
            try {
                Connection dbConnection = null;
                Statement statement = null;

                String insertTableSQL = "INSERT INTO Location"
                        + "(VehicleID, Latitude, Longitude, Speed) " + "VALUES"
                        + "(" + strings[0] + "," + strings[1] + "," + strings[2] + "," + strings[3] + ")";
                try {
                    Class.forName(DB_DRIVER);
                    dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                    statement = dbConnection.createStatement();
                    statement.executeUpdate(insertTableSQL);
                } catch (Exception e) {
                    System.out.println("Error Occured!");
                    System.out.println(e.getMessage());

                } finally {

                    if (statement != null) {
                        statement.close();
                    }

                    if (dbConnection != null) {
                        dbConnection.close();
                    }

                }
            } catch (SQLException ex) {
                ex.getMessage();
            }
            return null;
        }

    }
    private void updateRemote(int vid, double latitude, double longitude, double speed) {
        Log.e("Latitude:", latitude + "");
        Log.e("Longitude:", longitude + "");
        Log.e("Speed:", speed + "");
        new SendLocationData().execute(vid+"", latitude+"",longitude+"",speed+"");
        //Now that the local visualizations are out of the way, let's actually send it to the server.

    }
}
