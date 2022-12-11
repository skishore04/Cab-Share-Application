package com.example.mycabshare;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mycabshare.Model.DriverGeoModel;
import com.example.mycabshare.Remote.IGoogleAPI;
import com.example.mycabshare.Remote.RetrofitClient;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Common {

    public static final String baseURL = "https://googleapis.com";

    public static final String NOTI_TITLE = "title";
    public static final String NOTI_CONTENT = "body";

    public static final String TOKEN_REFERENCE = "Token";
    public static final String DRIVERS_LOCATION_REFERNCES ="DriversLocation" ;
    public static final String DRIVERS_INFO_REFERNCES = "DriverInfo" ;
    public static Set<DriverGeoModel> driversFound = new HashSet<DriverGeoModel>();
    public static HashMap<String, Marker> markerList = new HashMap<>();

    public static void showNotification(Context context, int id, String title, String body, Intent intent) {
        PendingIntent pendingIntent =null;
        if(intent != null)
            pendingIntent= PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "Klh CabShare";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Klh CabShare",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Klh CabShare");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.caricon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.caricon));

        if (pendingIntent !=null)
        {
            builder.setContentIntent(pendingIntent);
        }
        Notification notification = builder.build();
        notificationManager.notify(id,notification);

    }

    public static String buildName(String firstName, String lastName) {
        return new StringBuilder(firstName).append(" ").append(lastName).toString();
    }

    public static IGoogleAPI getGoogleApi()
    {
        return RetrofitClient.getClent(baseURL).create(IGoogleAPI.class);
    }
}
