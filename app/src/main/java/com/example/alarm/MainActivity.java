package com.example.alarm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /*
     * firebase 관련 전역변수
     */
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;
    private FirebaseFirestore db;
    private static final String TAG1 = "FIREAUTH";
    private static final String TAG2 = "FIRESTORE PUT";
    private static final String TAG3 = "FIRESTORE GET";

    /*
     * xml 레이아웃/뷰 관련 전역변수
     */
    private LinearLayout calenderLayout;
    private TextView today;
    private MaterialCalendarView calendarView;
    private LinearLayout mainLayout, information;
    private LinearLayout todo1, todo2, todo3, todo4, todo5, todo6, todo7, add;
    public static int todoCount = 2; //생성된 투두리스트 개수
    private DocumentReference document;
    private TimePicker timePicker;

    /*
     * 타임피커 관련 전역변수
     */
    // https://junghn.tistory.com/entry/JAVA-%EC%9E%90%EB%B0%94-%EB%82%A0%EC%A7%9C-%ED%8F%AC%EB%A7%B7-%EB%B3%80%EA%B2%BD-%EB%B0%A9%EB%B2%95SimpleDateFormat-yyyyMMdd
    private Date now = new Date();
    private Timestamp today_timestamp = new Timestamp(new Date());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //필드 이름으로 들어갈 string 생성
//    private String dateFormat_string = "2022-12-08";
    private String dateFormat_string = dateFormat.format(now);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("a hh mm");
    private String timeFormat_string = timeFormat.format(now); //나중에 이 string의 time만 수정해서 다시 변환할거임
    private SimpleDateFormat finalFormat = new SimpleDateFormat("yyyy-MM-dd a hh mm");
    private String finalFormat_string = finalFormat.format(now); //set 타임스탬프에 들어갈 시간 변수 미리 만들어놓기
    private Date selectedTime_info = finalFormat.parse(finalFormat_string); //time만 수정해서 다시 변환한
    private Timestamp selectedTime_timestamp;

    /*
     * 알람 관련 전역변수
     */
    public static Context context;
    private AlarmManager alarm_manager; // 알람매니저 설정
    final Calendar calendar = Calendar.getInstance(); // Calendar 객체 생성
    private Intent my_intent;
    private PendingIntent pendingIntent;
    private ActivityResultLauncher<Void> overlayPermissionLauncher;
    private static final int PERMISSION_REQUEST = 0;
    /*
     * 캘린더뷰 관련 전역변수
     */
    private Map<String,Object> todos = new HashMap<>();
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> arrayList;


    public MainActivity() throws ParseException {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        calenderLayout = findViewById(R.id.calender);
        mainLayout = findViewById(R.id.main);
        information = findViewById(R.id.information);
//        editText = findViewById(R.id.todo_text);
        todo1 = findViewById(R.id.todo1);
        todo2 = findViewById(R.id.todo2);
        todo3 = findViewById(R.id.todo3);
        todo4 = findViewById(R.id.todo4);
        todo5 = findViewById(R.id.todo5);
        todo6 = findViewById(R.id.todo6);
        todo7 = findViewById(R.id.todo7);
        add = findViewById(R.id.addTODO);
        today = findViewById(R.id.today);
        listView=findViewById(R.id.list_view);
        timePicker = findViewById(R.id.time_picker);
        calendarView = findViewById(R.id.calendarView);

        calenderLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

        findViewById(R.id.tab1).setOnClickListener(this);
        findViewById(R.id.tab2).setOnClickListener(this);
        findViewById(R.id.tab3).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        findViewById(R.id.today).setOnClickListener(this);
        mainLayout.setOnClickListener(this);
        add.setOnClickListener(this);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                for(String key : todos.keySet()) {
                    if (key.equals(dateFormat.format(date.getDate()))) {
                        Todo data = (Todo) todos.get(key);
                        arrayList = data.getTodoData();
                        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
//                        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_expandable_list_item_1, arrayList);
                        listView.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                        information.setVisibility(View.VISIBLE);
                        break;
                    }
                    else {
                        information.setVisibility(View.GONE);
                    }
                }
            }
        });

//        findViewById(R.id.todo_text).setOnClickListener(this);


        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);

        today.setText(dateFormat_string);
        calendarView.setSelectedDate(now);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        loginCheck();

        // 최초 실행 여부 판단
        // https://kiwinam.com/posts/1/android-shared-preferences/
        SharedPreferences pref = getSharedPreferences("sFile", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("sFile", false);
        if(first==false){
            //앱 최초 실행시 초기 설정 화면 실행
            onCheckPermission();
            my_intent = new Intent(this.context, InitAlram.class);
            startActivity(my_intent);
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start Activity.
                loginCheck();
                alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);

                today.setText(dateFormat_string);
                calendarView.setSelectedDate(now);
                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();
                // Access a Cloud Firestore instance from your Activity
                db = FirebaseFirestore.getInstance();
            } else {

                // Request for overlay permission.
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
//                Uri uri = Uri.fromParts("package",
//                        BuildConfig.APPLICATION_ID, null);
//                intent.setData(uri);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("다른 앱 위에 표시")
                        .setMessage("알람 팝업을 표시하기 위해 저장된 경로에 접근할 수 있는 권한이 필요합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();


                Snackbar.make(mainLayout, R.string.window_permission_denied,
                                Snackbar.LENGTH_SHORT).show();
                loginCheck();
                alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);

                today.setText(dateFormat_string);
                calendarView.setSelectedDate(now);
                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();
                // Access a Cloud Firestore instance from your Activity
                db = FirebaseFirestore.getInstance();
            }

        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void onCheckPermission() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this, Manifest.permission.MEDIA_CONTENT_CONTROL) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start
//            startCamera();
            requestWindowPermission();
        } else {
            // Permission is missing and must be requested.
            requestWindowPermission();
        }
        // END_INCLUDE(startCamera)
    }

    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestWindowPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            // Request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.ACCESS_NOTIFICATION_POLICY},
                    PERMISSION_REQUEST);

        } else {
            Snackbar.make(mainLayout, "R.string.camera_unavailable", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.SYSTEM_ALERT_WINDOW},
                    PERMISSION_REQUEST);
        }
    }

    /* 레이아웃 관련 */
    // 이벤트 발생 시 동작 함수
    // 클릭 이벤트
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        int v = view.getId();
        if (v == R.id.tab1) {
            calenderLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
            calenderLayout.setVisibility(View.VISIBLE);
        }
        else if (v == R.id.tab2) {
            calenderLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);

        }
        else if (v == R.id.tab3) {
            my_intent = new Intent(this, SettingsActivity.class);
            startActivity(my_intent);
        }
        else if (v == R.id.addTODO) addTODO();
        else if (v == R.id.ok) {
            sendTODO();
            setAlarm();
            my_intent = new Intent(this, AlarmRunning.class);
            startActivity(my_intent);
        }
        else if (v == R.id.main) {
            hideKeyboard();
        }
        else if (v == R.id.today) {
            calendarView.setSelectedDate(now);
        }
    }

    // 키보드 내리기
    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager.isAcceptingText()) inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // todo_list_design.xml 에서 자식 View 가져오기
    public static View getChildView(LinearLayout layout, int id) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view.getId() == id) {
                return view;
            }
        }
        return null;
    }

    // 알람 시간 맞추기
    private void setAlarm() {

        // reveiver에 string 값 넘겨주기
        my_intent = new Intent(this.context, AlarmReceiver.class);
        my_intent.putExtra("state","alarm on");
        my_intent.putExtra("array", arrayList);
        Log.d("RINGG", String.valueOf(arrayList) + " in MainActivity");

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent,
                PendingIntent.FLAG_IMMUTABLE);
        // 알람셋팅
        alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
    }


    // 중복 기능 동작 함수
    // DB로 데이터 전송
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendTODO() {
        // 고른 시간 값 가져오기
        // calendar에 시간 셋팅
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        int selectedH_int = timePicker.getHour();
        int selectedM_int = timePicker.getMinute();
        String selectedH = String.valueOf(selectedH_int);
        String selectedM = String.valueOf(selectedM_int);
        String AM_PM = "AM";
        if (selectedH_int > 11) {
            AM_PM = "PM";
            selectedH = String.valueOf(selectedH_int - 12);
        }
        timeFormat_string = AM_PM+" "+selectedH+" "+selectedM;
        finalFormat_string = String.format("%s %s", dateFormat_string, timeFormat_string);
        try {
            selectedTime_info = finalFormat.parse(finalFormat_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
//        cal.setTime(selectedTime_info);
        cal.setTimeInMillis(System.currentTimeMillis());
        // 현재시각을 기준으로 아침 알람 맞추기
        if (now.after(selectedTime_info)){
            cal.add(Calendar.DATE, 1); // 다음날(1일 후)
            calendar.add(Calendar.DATE, 1); // 다음날(1일 후)
        }
        selectedTime_info = calendar.getTime();
        selectedTime_timestamp = new Timestamp(selectedTime_info);

        // 작성한 투두리스트 가져오기
        EditText editText;
        String text;
        ArrayList<String> todo = new ArrayList<String>();
        switch (todoCount) {
            case 7:
                editText = (EditText) getChildView(todo7, R.id.todo_text);
                text = String.valueOf(editText.getText());
                if (!text.equals("")) todo.add(0, text);
            case 6:
                editText = (EditText) getChildView(todo6, R.id.todo_text);
                text = String.valueOf(editText.getText());
                if (!text.equals("")) todo.add(0, text);
            case 5:
                editText = (EditText) getChildView(todo5, R.id.todo_text);
                text = String.valueOf(editText.getText());
                if (!text.equals("")) todo.add(0, text);
            case 4:
                editText = (EditText) getChildView(todo4, R.id.todo_text);
                text = String.valueOf(editText.getText());
                if (!text.equals("")) todo.add(0, text);
            case 3:
                editText = (EditText) getChildView(todo3, R.id.todo_text);
                text = String.valueOf(editText.getText());
                if (!text.equals("")) todo.add(0, text);
            case 2:
                editText = (EditText) getChildView(todo2, R.id.todo_text);
                text = String.valueOf(editText.getText());
                if (!text.equals("")) todo.add(0, text);
            case 1:
                editText = (EditText) getChildView(todo1, R.id.todo_text);
                text = String.valueOf(editText.getText());
                if (!text.equals("")) todo.add(0, text);
                else todo.add(0, getString(R.string.빈_투두));
        }
        Map<String,Object> data = makeStruct(today_timestamp, selectedTime_timestamp, todo);

        // 필드 업데이트
        updateDocumentArray(data);
        Log.d(TAG2, "데이터 업데이트 완료");
    }

    // TODOLIST 생성
    public void addTODO() {
        if (todoCount == 6) {
            Snackbar.make(mainLayout, R.string.할일추가제한, Snackbar.LENGTH_SHORT).show();
        } else {
            todoCount++;
            if (todoCount == 2) todo3.setVisibility(View.VISIBLE);
            else if (todoCount == 3) todo4.setVisibility(View.VISIBLE);
            else if (todoCount == 4) todo5.setVisibility(View.VISIBLE);
            else if (todoCount == 5) todo6.setVisibility(View.VISIBLE);
            else if (todoCount == 6) {
                todo7.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);
            }
        }
    }

    // TODOLIST 삭제
    public void deleteTODO() {
        if (todoCount == 0) {
            Snackbar.make(calenderLayout, R.string.할일추가제한, Snackbar.LENGTH_SHORT).show();
        } else {
            todoCount--;
            if (todoCount == 2) todo3.setVisibility(View.INVISIBLE);
            else if (todoCount == 3) todo4.setVisibility(View.INVISIBLE);
            else if (todoCount == 4) todo5.setVisibility(View.INVISIBLE);
            else if (todoCount == 5) todo6.setVisibility(View.INVISIBLE);
            else if (todoCount == 6) {
                todo7.setVisibility(View.INVISIBLE);
                add.setVisibility(View.VISIBLE);
            }
        }
    }

    /* FIREBASE 관련 */
    // firebase-auth 관리 함수
    // 활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인
    public void loginCheck() {
        // Check if user is signed in (non-null) and update UI accordingly.
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG1, "signInAnonymously:success");
                                user = mAuth.getCurrentUser();
                                updateUI(user);
                                setDocument();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG1, "signInAnonymously:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }

    private void updateUI(FirebaseUser user) {
        ArrayList<CalendarDay> lightBlue = new ArrayList<>();
        ArrayList<CalendarDay> darkBlue = new ArrayList<>();
        //TODO test uid - 0PlX308YNEeleWsHBZNn4VfAAVf1
//        DocumentReference document = db.collection("user").document("0PlX308YNEeleWsHBZNn4VfAAVf1");
        DocumentReference document = db.collection("user").document(user.getUid());
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> date = document.getData();
                        for(String key : date.keySet()){
//                            Log.d(TAG3, key + " = " + date.get(key));
                            Todo todo = new Todo();
                            Map<String,Object> data = (Map<String, Object>) date.get(key);
                            for(String key2 : data.keySet()){
                                if (key2.equals("todo")) todo.setTodoData((ArrayList<String>) data.get(key2));
                                else if (key2.equals("set")) todo.setSetTime((Timestamp) data.get(key2));
                                else if (key2.equals("create")) todo.setCreatedTime((Timestamp) data.get(key2));
                                else if (key2.equals("end")) todo.setEndTime((Timestamp) data.get(key2));
                            }

                            long time = todo.getEndTime().getSeconds() - todo.getSetTime().getSeconds();
                            Log.d(TAG3, String.valueOf(time));
                            CalendarDay myDate = CalendarDay.from(todo.getCreatedTime().toDate());
                            Log.d(TAG3, String.valueOf(CalendarDay.from(todo.getCreatedTime().toDate())));
                            if (time > 300) {
                                darkBlue.add(myDate);
                            }
                            else {
                                lightBlue.add(myDate);
                            }

                            todos.put(key, todo);
//                            arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_expandable_list_item_1, items);
//                            listView.setAdapter(arrayAdapter);
//                            Log.d(TAG3, String.valueOf(lightBlue));
//                            Log.d(TAG3, String.valueOf(darkBlue));
                        }
//                        Log.d("FINALL", String.valueOf(todos));

                        // 캘린더에 띄우기. EventDecorator 활용
                        EventDecorator eventDecorator1 = new EventDecorator(R.color.Secondary, lightBlue);
                        EventDecorator eventDecorator2 = new EventDecorator(R.color.SecondaryVariant, darkBlue);
                        calendarView.addDecorators(eventDecorator1, eventDecorator2);
//                        Log.d("FINALL", String.valueOf(lightBlue));
//                        Log.d("FINALL", String.valueOf(darkBlue));

                        // 캘린더 들어가면 오늘의 투두리스트 정보가 바로 나오도록
                        Date tmp = now;
                        try {
                            tmp = dateFormat.parse(dateFormat_string);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        for(String key : todos.keySet()) {
                            if (key.equals(dateFormat.format(tmp))) {
                                Todo data = (Todo) todos.get(key);
                                arrayList = data.getTodoData();
                                arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
//                        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_expandable_list_item_1, arrayList);
                                listView.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                                information.setVisibility(View.VISIBLE);
                                break;
                            }
                            else {
                                information.setVisibility(View.GONE);
                            }
                        }


//                        Log.d(TAG3, "DocumentSnapshot data: " + date.get("2022-12-06"));
                    } else {
                        Log.d(TAG3, "No such document with UID : " + user.getUid());
                        setDocument();
                    }
//                    Log.d(TAG3, String.valueOf(todos));

                } else {
                    Log.d(TAG3, "get failed with ", task.getException());
                }
            }
        });
//        CalendarDay mydate= CalendarDay.from(2020,  12, 31); // year, month, date
//        EventDecorator eventDecorator = new EventDecorator();
//        calendarView.addDecorators(CurrentDayDecorator(this, mydate))
    }

    // firebase-firestore 관리 함수
    // 데이터 저장 형식
    // user > 유저 UID > main (Map<String, Object>를 저장하는) 배열 필드 > Map<String, Object>
    //      Map<String, Object>는 4개의 데이터 저장
    //      1. "create" (타임스탬프) 해당 내용이 생성된 시간
    //      2. "end" (타임스탬프) 알람이 울릴 시간
    //      3. "set" (타임스탬프) 알람이 꺼진 시간 -> 나중에 추가됨(맞춘 알람 시간으로 찾아감)
    //      4. "todo_" (배열) 투두리스트에 작성한 내용

    // 저장할 객체 형식을 생성해봅시다
    private Map<String,Object> makeStruct(Timestamp create, Timestamp set, ArrayList<String> todo) {
        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("create", create); //타임스탬프 넣기
        tmp.put("set", set); //타임스탬프 넣기
        tmp.put("end", set); //타임스탬프 넣기... 나중에 main에 추가하는 방식으로 가야할듯. 맨 마지막 인덱스로 가서 고치는걸로!
        tmp.put("todo", todo); //배열 넣기
        return tmp;
    }

    // end 데이터를 수정합니다
    private Map<String,Object> modifyStruct(Map<String,Object> data, Timestamp end) {
        data.put("end", end);
        return data;
    }

    // 단일 문서 만들기. 문서 처음 생성 시(유저의 첫번째 알람 설정 시)에만 사용
    public void setDocument() {
        Map<String, Object> main = new HashMap<>();

        // 유저 UID로 문서 생성 후 그 안에 해당 유저에 대한 정보 삽입
        // 참고 - https://cloud.google.com/firestore/docs/samples/firestore-data-set-array-operations?hl=ko#firestore_data_set_array_operations-java
        //TODO test uid - 0PlX308YNEeleWsHBZNn4VfAAVf1
//        DocumentReference document = db.collection("user").document("0PlX308YNEeleWsHBZNn4VfAAVf1");
        DocumentReference document = db.collection("user").document(user.getUid());
        // 문서까지만 생성
        document
                .set(main)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG2, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG2, "Error writing document", e);
                    }
                });
        // [END set_document]
    }

    // DB에 데이터넣기
    public void updateDocumentArray(Map<String, Object> data) {
        // [START update_document_array]
        // 하루에 하나의 데이터만 생성.
        //TODO test uid - 0PlX308YNEeleWsHBZNn4VfAAVf1
//        DocumentReference document = db.collection("user").document("0PlX308YNEeleWsHBZNn4VfAAVf1");
        DocumentReference document = db.collection("user").document(user.getUid());

        // Atomically remove a new Map<> to the "main" array field.
        // document.update(dateFormat_string, FieldValue.arrayRemove(data));

        // Atomically add a new Map<> to the "main" array field.
        document.update(dateFormat_string, data);

        // [END update_document_array]
    }
}