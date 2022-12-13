package com.example.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        this.context = context;
        // intent로부터 전달받은 string
        String get_your_string = intent.getStringExtra("state");
        ArrayList<String> stringList = intent.getStringArrayListExtra("array");
        Log.d("RINGG", String.valueOf(stringList) + " in AlarmReceiver");

        // RingtonePlayingService 서비스 intent 생성
        Intent service_intent = new Intent(this.context, RingtonePlayingService.class);

        // RingtonePlayinService로 extra string값 보내기
        service_intent.putExtra("state", get_your_string);
        service_intent.putExtra("array", stringList);



        // start the ringtone service

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(service_intent);
        }else{
            this.context.startService(service_intent);
        }
    }
}