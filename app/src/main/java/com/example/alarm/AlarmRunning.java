package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

// 알람 설정 btn을 누르면 넘어오는 화면입니다. 해당 화면을 켜두고 자면 됩니다.
public class AlarmRunning extends AppCompatActivity implements View.OnClickListener {
    /*
     * xml 레이아웃/뷰 관련 전역변수
     */
    private Button cancel;
    private TextView currentDate;
    private TextView currentTime;
    private TextView currentAMPM;
    private LinearLayout running;
    /*
     * 알람 관련 전역변수
     */
    Context context;
    private AlarmManager alarm_manager; // 알람매니저 설정
    final Calendar calendar = Calendar.getInstance(); // Calendar 객체 생성
    private Intent my_intent;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_running);

        this.context = this;

        findViewById(R.id.cancel).setOnClickListener(this);
        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);

        running = findViewById(R.id.running);
        currentDate = findViewById(R.id.currentDate);
        currentTime = findViewById(R.id.currentTime);
        currentAMPM = findViewById(R.id.currentAMPM);

        Snackbar.make(running, R.string.설정완료알림, Snackbar.LENGTH_SHORT).show();
        // 쓰레드를 이용해서 시계표시
        // https://mycute7.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%8A%A4%ED%8A%9C%EB%94%94%EC%98%A4-%EC%8B%9C%EA%B3%84%EC%9C%84%EC%A0%AF-%EB%A7%8C%EB%93%A4%EA%B8%B0-Thread-Timer-TimerTask-%EC%82%AC%EC%9A%A9
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Calendar calendar = Calendar.getInstance(); // 칼렌다 변수
                            int year = calendar.get(Calendar.YEAR); // 년
                            int month = calendar.get(Calendar.MONTH); // 월
                            int day = calendar.get(Calendar.DAY_OF_MONTH); // 일
                            int am_pm = calendar.get(Calendar.AM);
                            int hour = calendar.get(Calendar.HOUR); // 시
                            int minute = calendar.get(Calendar.MINUTE); // 분

                            String AM_PM = getString(R.string.am);
                            if (am_pm != 0) AM_PM = getString(R.string.pm);
                            currentDate.setText(year + " " + month + " " + day);
                            currentTime.setText(hour + " : " + minute + " ");
                            currentAMPM.setText(AM_PM);
                        }
                    });
                    try {
                        Thread.sleep(1000); // 1000 ms = 1초
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void onClick(View view) {
        int v = view.getId();
        if (v == R.id.cancel){

            Intent my_intent = getIntent();
            ArrayList<String> stringList = my_intent.getStringArrayListExtra("array");

            my_intent = new Intent(this.context, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(AlarmRunning.this, 0, my_intent,
                    PendingIntent.FLAG_MUTABLE); //뭔지 잘 모름
            alarm_manager.cancel(pendingIntent);
            my_intent.putExtra("state","alarm off");
            my_intent.putExtra("array", stringList);
            Log.d("RINGG", String.valueOf(stringList) + " in AlarmRunning");


            // 알람취소
            sendBroadcast(my_intent);
            finish();
        }
    }
}