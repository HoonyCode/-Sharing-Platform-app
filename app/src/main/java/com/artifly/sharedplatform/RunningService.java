package com.artifly.sharedplatform;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.artifly.sharedplatform.MainActivity.Main_activity;

public class RunningService extends Service {
    private NotificationManager notificationManager;
    private String userId = "TEST";
    private String target = "1";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("11111", "oncreate");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("check", "onStartCommand() called");

        if (intent == null) {
            return Service.START_STICKY;
        } else {
            userId = intent.getStringExtra("id");
            target = intent.getStringExtra("target");
            Log.e("check", "전달받은 데이터: " + userId + target);
        }

        startForegroundService();

        return super.onStartCommand(intent, flags, startId);
    }

    public void startForegroundService(){
        Log.e("2222222", "service");

        Intent notificationIntent = new Intent(this, RunningActivity.class);
        notificationIntent.putExtra("id", userId);
        notificationIntent.putExtra("target", target);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder serviceNotificationBuilder;

        // 서비스 작동 중일때 표시되는 알림 설정
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("ch2", "서비스 작동 알림", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            serviceNotificationBuilder = new NotificationCompat.Builder(this, "ch2");
        }
        else{
            serviceNotificationBuilder = new NotificationCompat.Builder(this);
        }
        serviceNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Blocksum")
                .setContentText("작동 중...")
                .setTicker("작동 중...")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        // 포어그라운드 서비스 시작
        startForeground(2, serviceNotificationBuilder.build());
    }
}
