package com.example.drugrecoveryapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class BackgroundTimerService extends Service {

    private CountDownTimer backgroundTimer;

    private final IBinder binder = new LocalBinder();

    private static final long BACKGROUND_TIMER_INTERVAL = 1000; // 1 second interval

    public BackgroundTimerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startBackgroundTimer();
        return START_STICKY;
    }

    private void startBackgroundTimer() {
        backgroundTimer = new CountDownTimer(Long.MAX_VALUE, BACKGROUND_TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Handle background timer tick
                sendBroadcast(new Intent("BackgroundTimerTick")); // Broadcast intent for every tick
            }

            @Override
            public void onFinish() {
                // Handle timer finish (not applicable for a repeating timer)
            }
        };
        backgroundTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (backgroundTimer != null) {
            backgroundTimer.cancel();
        }
    }

    public class LocalBinder extends Binder {
        BackgroundTimerService getService() {
            return BackgroundTimerService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

