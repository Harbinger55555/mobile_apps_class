package com.techexchange.mobileapps.lab12;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class NotifierService extends Service {
    private static final String TAG = "NotifierService";
    static final String CHANNEL_ID = NotifierService.class.getName() + "pingchannel";
    private static final String CHANNEL_NAME = "30-sec ping";

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("This is the main channel for the thirty-second timer.");
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand NotifierService.");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called!");
        createNotificationChannel();
        NotifierThread thread = new NotifierThread(this);
        thread.start();
        thread.getLooper();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static Intent newIntent(Context context) {
        return new Intent(context, NotifierService.class);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved NotifierService.");
        super.onTaskRemoved(rootIntent);
    }
}
