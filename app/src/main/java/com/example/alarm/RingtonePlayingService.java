package com.example.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class RingtonePlayingService extends Service {
    public RingtonePlayingService() {
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    MediaPlayer mediaPlayer;
    int startId;
    boolean isRunning;
//    int music = R.raw.music2; //재생할 음악

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.music2);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("알람시작")
                    .setContentText("알람음이 재생됩니다.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String getState = intent.getExtras().getString("state");

        assert getState != null;
        switch (getState) {
            case "alarm on":
                startId = Service.START_STICKY;
                break;
            case "alarm off":
                startId = Service.START_STICKY_COMPATIBILITY;
                break;
            default:
                startId = Service.START_STICKY_COMPATIBILITY;
                break;
        }

        // 알람음 재생 X , alarm on 데이터 들어옴
        if(!this.isRunning && startId == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music2);
            mediaPlayer.start();
            this.isRunning = true;
            this.startId = 0;

            Intent showIntent = new Intent(getApplicationContext(), AlarmRinging.class);
            showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ArrayList<String> stringList = intent.getStringArrayListExtra("array");
            showIntent.putExtra("array", stringList);
            Log.d("RINGG", String.valueOf(stringList) + " in RingtonePlayingService");
            startActivity(showIntent);
        }

        // 알람음 재생 O , alarm off 데이터 들어옴
        else if(this.isRunning && startId == 0) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            this.isRunning = false;
            this.startId = 0;
        }

        // 알람음 재생 X , alarm off 데이터 들어옴
        else if(!this.isRunning && startId == 0) {

            this.isRunning = false;
            this.startId = 0;

        }

        // 알람음 재생 O , alarm on 데이터 들어옴
        else if(this.isRunning && startId == 1){

            this.isRunning = true;
            this.startId = 1;
        }

        else {
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("onDestory() 실행", "서비스 파괴");

    }
}