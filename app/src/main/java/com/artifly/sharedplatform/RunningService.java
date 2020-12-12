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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class RunningService extends Service {
    private NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopForeground(true);
    }

    public void startForegroundService(){
        Intent notificationIntent = new Intent(this, RunningActivity.class);
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
