package com.example.alarm;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


// 정한 시간이 되었을 때, 즉 알람이 울릴 때 뜨는 미션창입니다.
public class AlarmRinging extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private TextView currentDate;
    private TextView currentTime;
    private TextView currentAMPM;
    private View todo1, todo2, todo3, todo4, todo5, todo6, todo7;
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
    ArrayList<String> stringList;
    ArrayList<CheckBox> tmp = new ArrayList<>();
    boolean checked = true;



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
        checkBox1 = todo1.findViewById(R.id.checkbox);
        checkBox2 = todo2.findViewById(R.id.checkbox);
        checkBox3 = todo3.findViewById(R.id.checkbox);
        checkBox4 = todo4.findViewById(R.id.checkbox);
        checkBox5 = todo5.findViewById(R.id.checkbox);
        checkBox6 = todo6.findViewById(R.id.checkbox);
        checkBox7 = todo7.findViewById(R.id.checkbox);
        checkBox1.setOnCheckedChangeListener(this);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3.setOnCheckedChangeListener(this);
        checkBox4.setOnCheckedChangeListener(this);
        checkBox5.setOnCheckedChangeListener(this);
        checkBox6.setOnCheckedChangeListener(this);
        checkBox7.setOnCheckedChangeListener(this);

        editText1 = todo1.findViewById(R.id.todo_text);
        editText2 = todo2.findViewById(R.id.todo_text);
        editText3 = todo3.findViewById(R.id.todo_text);
        editText4 = todo4.findViewById(R.id.todo_text);
        editText5 = todo5.findViewById(R.id.todo_text);
        editText6 = todo6.findViewById(R.id.todo_text);
        editText7 = todo7.findViewById(R.id.todo_text);
        currentDate = findViewById(R.id.currentDate);
        currentTime = findViewById(R.id.currentTime);
        currentAMPM = findViewById(R.id.currentAMPM);


        Intent intent = getIntent();
        stringList = intent.getStringArrayListExtra("array");
        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Log.d("RINGG", String.valueOf(stringList) + " in AlarmRinging");


        int cnt = stringList.size()-1;
        switch (stringList.size()-1) {
            case 6:
                editText7.setText(stringList.get(cnt--));
                todo7.setVisibility(View.VISIBLE);
                tmp.add(0, checkBox7);
            case 5:
                editText6.setText(stringList.get(cnt--));
                todo6.setVisibility(View.VISIBLE);
                tmp.add(0, checkBox6);
            case 4:
                editText5.setText(stringList.get(cnt--));
                todo5.setVisibility(View.VISIBLE);
                tmp.add(0, checkBox5);
            case 3:
                editText4.setText(stringList.get(cnt--));
                todo4.setVisibility(View.VISIBLE);
                tmp.add(0, checkBox4);
            case 2:
                editText3.setText(stringList.get(cnt--));
                todo3.setVisibility(View.VISIBLE);
                tmp.add(0, checkBox3);
            case 1:
                editText2.setText(stringList.get(cnt--));
                todo2.setVisibility(View.VISIBLE);
                tmp.add(0, checkBox2);
            case 0:
                editText1.setText(stringList.get(cnt));
                tmp.add(0, checkBox1);
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

    public void turnOffAlarm() {
        Log.d("RINGG", "알람 끄기");
        // calendar에 꺼진시간 저장
        time = calendar.getTime();

        // reveiver에 string 값 넘겨주기
        my_intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, my_intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (int i=0; i<tmp.size(); i++)
        {
            if (!(tmp.get(i).isChecked())) {
                return;
            }
        }
        turnOffAlarm();
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