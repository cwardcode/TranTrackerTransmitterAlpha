package com.cwardcode.TranTracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;

/**
 * User: Chris
 * Date: 7/23/13
 * Time: 1:21 PM
 */
public class SendLoc extends Service {
    private LocationManager lm;
    private LocationListener locListener;
    private static int vehicleID;
    private class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            if(location != null) {
                updateLocation(location);
            }
        }
        @Override
        public void onProviderEnabled(String str){}
        @Override
        public void onProviderDisabled(String str){}
        @Override
        public void onStatusChanged(String str, int i, Bundle bundle){}
    }

    public void onCreate() {
        super.onCreate();
    }
    public void onStart(Intent intent, int startID){
        super.onStart(intent, startID);
        addLocationListener();
        vehicleID = intent.getIntExtra("VehicleID", -1);
    }

    private void addLocationListener() {
        Thread sendLocThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Looper.prepare();
                    lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                    Criteria c = new Criteria();
                    c.setAccuracy(Criteria.ACCURACY_FINE);

                    locListener = new MyLocationListener();
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);
                    Looper.loop();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }, "SendLocThread");

        sendLocThread.start();
    }

    public static void updateLocation(Location location)
    {
        Context context;
        context = TranTracker.getAppContext();

        double latitude, longitude, speed;

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        speed = location.getSpeed();

        Intent filterRes = new Intent();
        filterRes.setAction("com.cwardcode.intent.action.LOCATION");
        filterRes.putExtra("latitude", latitude);
        filterRes.putExtra("longitude", longitude);
        filterRes.putExtra("speed", speed);
        filterRes.putExtra("VehicleID",vehicleID);
        context.sendBroadcast(filterRes);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
