package com.example.alarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import org.checkerframework.checker.units.qual.C;

public class InitAlram extends AppCompatActivity implements View.OnClickListener {

    /*
     * xml 레이아웃/뷰 관련 전역변수
     */
    private LinearLayout push_alarm_setting, repeat_alarm_setting,
            push_alarm_check_toggle, repeat_alarm_check_toggle;
    private Switch push_alarm_switch, repeat_alarm_switch;
    private TimePicker push_timepicker, default_timepicker;
    int setHour;
    int setMin;

    SharedPreferences sf = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = sf.edit();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_alram);

        push_alarm_setting = findViewById(R.id.push_alarm);
        repeat_alarm_setting = findViewById(R.id.repeat_alarm);
        push_alarm_switch = findViewById(R.id.push_alarm_switch);
        repeat_alarm_switch = findViewById(R.id.repeat_alarm_switch);
        push_alarm_check_toggle = findViewById(R.id.push_alarm_check_toggle);
        repeat_alarm_check_toggle = findViewById(R.id.repeat_alarm_check_toggle);
        push_timepicker = findViewById(R.id.init_push_alarm_time_picker);
        default_timepicker = findViewById(R.id.repeat_alarm_time_picker);


        findViewById(R.id.push_alarm_nextbutton).setOnClickListener(this);
        findViewById(R.id.push_alarm_switch).setOnClickListener(this);
        findViewById(R.id.repeat_alarm_nextbutton).setOnClickListener(this);
        findViewById(R.id.repeat_alarm_backbutton).setOnClickListener(this);
        findViewById(R.id.repeat_alarm_switch).setOnClickListener(this);



        // todo 초기 세팅
        //  1. 시스템 설정 변경 -> 뮤직 볼륨 설정
        //  2. 저장소 접근 권한 -> 음악 파일을 재생하기 위해 저장된 경로에 접근할 수 있는 권한
        //  3. 다른 앱 위에 표시 -> 휴대폰 잠금 상태에서도 팝업 뜨도록
        // 권한 설정 방법
        // 설정 > 애플리케이션 > 우리 어플 > 권한에서 변경 가능

        // 1. 시스템 설정 변경 창으로 이동해서 허용하도록 함. 토스트 띄워서 설정해달라고 요청함
        // 참고 : https://kanais2.tistory.com/291
//        Intent intent = new Intent();
//        intent.setClassName( "com.android.settings" , "com.android.settings.SoundSettings" );
//        startActivity(intent);

        // 2.
        //


        // 3.
        //

    }

    @Override
    public void onClick(View view) {


        editor.putInt("set_pushHour",0);
        editor.putInt("set_pushMin",0);
        editor.putInt("set_defaultHour",0);
        editor.putInt("set_defaultMin",0);

        int v = view.getId();
        if (v == R.id.push_alarm_nextbutton){
            push_alarm_setting.setVisibility(View.GONE);
            repeat_alarm_setting.setVisibility(View.GONE);
            repeat_alarm_setting.setVisibility(View.VISIBLE);
        }
        else if (v == R.id.repeat_alarm_backbutton){
            push_alarm_setting.setVisibility(View.GONE);
            repeat_alarm_setting.setVisibility(View.GONE);
            push_alarm_setting.setVisibility(View.VISIBLE);
        }
        else if (v == R.id.repeat_alarm_nextbutton){
            // todo 두 기능 모두 미사용 시 주의 다이얼로그 띄우기
            finish();
            editor.putBoolean("isFirst",true);
            editor.commit();
        }
        else if (v == R.id.push_alarm_switch){
            if (push_alarm_switch.isChecked()){
                push_alarm_check_toggle.setVisibility(View.VISIBLE);

                push_timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                        editor.putInt("setHour",hour);
                        editor.putInt("setMin",min);
                        editor.commit();
                    }
                });
            }else push_alarm_check_toggle.setVisibility(View.INVISIBLE);
        }
        else if (v == R.id.repeat_alarm_switch){
            if (repeat_alarm_switch.isChecked()){
                repeat_alarm_check_toggle.setVisibility(View.VISIBLE);

                default_timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                        editor.putInt("setdefaultHour",hour);
                        editor.putInt("setdefaultMin",min);
                        editor.commit();
                    }
                });
            }else repeat_alarm_check_toggle.setVisibility(View.INVISIBLE);
        }
    }



}