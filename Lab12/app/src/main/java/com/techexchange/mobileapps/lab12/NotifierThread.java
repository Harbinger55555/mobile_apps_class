package com.techexchange.mobileapps.lab12;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

final class NotifierThread extends HandlerThread {
    private static final String TAG = "NotifierThread";

    private Handler handler;
    private Context context;

    NotifierThread(Context context) {
        super(TAG);
        this.context = context;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new NotificationHandler(context);
        handler.obtainMessage(0, null).sendToTarget();
    }
}

