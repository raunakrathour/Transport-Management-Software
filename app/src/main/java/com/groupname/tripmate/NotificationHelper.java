package com.groupname.tripmate;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.List;

public class NotificationHelper {
    private Context mContext;
    private List<Location> mLocationList;

    public NotificationHelper(Context mContext) {
        this.mContext = mContext;
        this.mLocationList = mLocationList;
    }
    public void showNotificationForDriver(String s)
    {
        Intent notificationIntent = new Intent(mContext,MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = null;
        notificationBuilder = new NotificationCompat.Builder(mContext,
                FirstClass.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("TripMate")
                .setContentText(s)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getNotificationManager().notify(0,notificationBuilder.build());
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private NotificationManager getNotificationManager()
    {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }
    public void showNotificationForUser()
    {

    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Location> getmLocationList() {
        return mLocationList;
    }

    public void setmLocationList(List<Location> mLocationList) {
        this.mLocationList = mLocationList;
    }
}

