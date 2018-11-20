package com.techexchange.mobileapps.lab12;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;


final class NotificationHandler extends Handler {
    private static final String TAG = "NotificationHandler";

    private Context context;

    NotificationHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        Notification notification = new NotificationCompat.Builder(context,
                NotifierService.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Ping Notification")
                .setContentText("This is your 30-second reminder number: " + msg.arg1)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(0, notification);
        Message message = new Message();
        message.arg1 = msg.arg1 + 1;
        sendMessageDelayed(message,3000);
    }

}

