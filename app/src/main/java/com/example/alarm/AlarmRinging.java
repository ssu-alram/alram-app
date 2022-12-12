package com.example.alarm;

import static com.example.alarm.MainActivity.getChildView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


// 정한 시간이 되었을 때, 즉 알람이 울릴 때 뜨는 미션창입니다.
public class AlarmRinging extends AppCompatActivity implements View.OnClickListener{

    private TextView currentDate;
    private TextView currentTime;
    private TextView currentAMPM;
    private LinearLayout todo1, todo2, todo3, todo4, todo5, todo6, todo7;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7;
    private EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;

    /*
     * 알람 관련 전역변수
     */
    Context context;
    private AlarmManager alarm_manager; // 알람매니저 설정
    final Calendar calendar = Calendar.getInstance(); // Calendar 객체 생성
    private Intent my_intent;
    private PendingIntent pendingIntent;
    Date time; //알람 꺼진 시각 저장

    private static PowerManager.WakeLock sCpuWakeLock;



    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );


        todo1 = findViewById(R.id.todo1);
        todo2 = findViewById(R.id.todo2);
        todo3 = findViewById(R.id.todo3);
        todo4 = findViewById(R.id.todo4);
        todo5 = findViewById(R.id.todo5);
        todo6 = findViewById(R.id.todo6);
        todo7 = findViewById(R.id.todo7);
        currentDate = findViewById(R.id.currentDate);
        currentTime = findViewById(R.id.currentTime);
        currentAMPM = findViewById(R.id.currentAMPM);

        Intent intent = getIntent();
        ArrayList<String> stringList = intent.getStringArrayListExtra("array");
        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Log.d("RINGG", String.valueOf(stringList) + " in AlarmRinging");


        int cnt = stringList.size()-1;
        switch (stringList.size()-1) {
            case 6:
                checkBox7 = (CheckBox) getChildView(todo7, R.id.checkbox);
                editText7 = (EditText) getChildView(todo7, R.id.todo_text);
                checkBox7.setOnClickListener(this);
                editText7.setText(stringList.get(cnt--));
                todo7.setVisibility(View.VISIBLE);
            case 5:
                checkBox6 = (CheckBox) getChildView(todo6, R.id.checkbox);
                editText6 = (EditText) getChildView(todo6, R.id.todo_text);
                checkBox6.setOnClickListener(this);
                editText6.setText(stringList.get(cnt--));
                todo6.setVisibility(View.VISIBLE);
            case 4:
                checkBox5 = (CheckBox) getChildView(todo5, R.id.checkbox);
                editText5 = (EditText) getChildView(todo5, R.id.todo_text);
                checkBox5.setOnClickListener(this);
                editText5.setText(stringList.get(cnt--));
                todo5.setVisibility(View.VISIBLE);
            case 3:
                checkBox4 = (CheckBox) getChildView(todo4, R.id.checkbox);
                editText4 = (EditText) getChildView(todo4, R.id.todo_text);
                checkBox4.setOnClickListener(this);
                editText4.setText(stringList.get(cnt--));
                todo4.setVisibility(View.VISIBLE);
            case 2:
                checkBox3 = (CheckBox) getChildView(todo3, R.id.checkbox);
                editText3 = (EditText) getChildView(todo3, R.id.todo_text);
                checkBox3.setOnClickListener(this);
                editText3.setText(stringList.get(cnt--));
                todo3.setVisibility(View.VISIBLE);
            case 1:
                checkBox2 = (CheckBox) getChildView(todo2, R.id.checkbox);
                editText2 = (EditText) getChildView(todo2, R.id.todo_text);
                checkBox2.setOnClickListener(this);
                editText2.setText(stringList.get(cnt--));
                todo2.setVisibility(View.VISIBLE);
            case 0:
                checkBox1 = (CheckBox) getChildView(todo1, R.id.checkbox);
                editText1 = (EditText) getChildView(todo1, R.id.todo_text);
                checkBox1.setOnClickListener(this);
                editText1.setText(stringList.get(cnt));
        }

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
        if (view == checkBox1) if (isItAllChecked()) turnOffAlarm();
        else if (view == checkBox2) if (isItAllChecked()) turnOffAlarm();
        else if (view == checkBox3) if (isItAllChecked()) turnOffAlarm();
        else if (view == checkBox4) if (isItAllChecked()) turnOffAlarm();
        else if (view == checkBox5) if (isItAllChecked()) turnOffAlarm();
        else if (view == checkBox6) if (isItAllChecked()) turnOffAlarm();
        else if (view == checkBox7) if (isItAllChecked()) turnOffAlarm();
    }

    public boolean isItAllChecked() {
        CheckBox[] tmp = {checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7};
        for (int i=0; i<tmp.length; i++){
            if (tmp[i].getVisibility() != View.VISIBLE){
                if (!tmp[i].isChecked()) return false;
            }
            else break;
        }
        return true;
    }

    public void turnOffAlarm() {
        Log.d("RINGG", "알람 끄기");
        // calendar에 꺼진시간 저장
        time = calendar.getTime();

        // reveiver에 string 값 넘겨주기
        my_intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, my_intent,
                PendingIntent.FLAG_MUTABLE);

        // 알람매니저 취소
        alarm_manager.cancel(pendingIntent);
        my_intent.putExtra("state","alarm off");
        // 알람취소
        sendBroadcast(my_intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }

//    You cannot disable home button. 안먹힘.
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) return true;
//        return super.dispatchKeyEvent(event);
//    };
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_HOME:
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_HOME:
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//        onBackPressed();
//
//    }

}