package com.example.alarm;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ticker.core.ui.Ticker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*
     * firebase 관련 전역변수
     */
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;
    private FirebaseFirestore db;
    private static final String TAG1 = "FIREAUTH";
    private static final String TAG2 = "FIRESTORE";
    /*
     * xml 레이아웃/뷰 관련 전역변수
     * todoCount - 생성된 투두리스트 개수
     */
    private LinearLayout calenderLayout;
    private LinearLayout mainLayout;
    private LinearLayout todo1, todo2, todo3, todo4, todo5, todo6, todo7, add;
    private int todoCount = 2;
    private DocumentReference document;

    /*
     * 외부 라이브러리 관련 전역변수 (타임피커, 캘린더뷰)
     * todoCount - 생성된 투두리스트 개수
     */
    private Ticker ticker;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calenderLayout = findViewById(R.id.calender);
        mainLayout = findViewById(R.id.main);
//        editText = findViewById(R.id.todo_text);
        todo1 = findViewById(R.id.todo1);
        todo2 = findViewById(R.id.todo2);
        todo3 = findViewById(R.id.todo3);
        todo4 = findViewById(R.id.todo4);
        todo5 = findViewById(R.id.todo5);
        todo6 = findViewById(R.id.todo6);
        todo7 = findViewById(R.id.todo7);
        add = findViewById(R.id.addTODO);
        ticker = findViewById(R.id.time_picker);

        calenderLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

        findViewById(R.id.tab1).setOnClickListener(this);
        findViewById(R.id.tab2).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        mainLayout.setOnClickListener(this);
        add.setOnClickListener(this);
//        findViewById(R.id.todo_text).setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG1, "signInAnonymously:success");
                            user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG1, "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

//        // 출처 - https://recipes4dev.tistory.com/48
//        items = new ArrayList<String>();
//        // 문제 해결 - https://stackoverflow.com/questions/28192815/how-to-add-customised-layout-to-arrayadapter
//        adapter = new ArrayAdapter(this, R.layout.todo_list_design, R.id.todo_text, items);
//        listview = findViewById(R.id.listview);
//        listview.setAdapter(adapter);
//        addTODO();
//        addTODO();
    }

    /* 레이아웃 관련 */
    // 이벤트 발생 시 동작 함수
    // 클릭 이벤트
    @Override
    public void onClick(View view) {
        int v = view.getId();
        if (v == R.id.tab1) {
            calenderLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
            calenderLayout.setVisibility(View.VISIBLE);
        } else if (v == R.id.tab2) {
            calenderLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        } else if (v == R.id.addTODO) addTODO();
        else if (v == R.id.ok) {
//            sendTODO();
            String timeSelected = ticker.getCurrentlySelectedTime();
            Log.d("시간", timeSelected);
//            Log.d("시간", "시간");
        }
        else if (v == R.id.main) {
            hideKeyboard();
        }
    }

    // 키보드 내리기 이벤트
    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // todo_list_design.xml 에서 자식 View 가져오기
    private View getChildView(LinearLayout layout, int id) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view.getId() == id) {
                return view;
            }
        }
        return null;
    }

    // 중복 기능 동작 함수
    // DB로 데이터 전송
    private void sendTODO() {
        EditText editText;
        switch (todoCount) {
            case 7:
                editText = (EditText) getChildView(todo7, R.id.todo_text);
                //todo firestore에 값 넣기
            case 6:
                editText = (EditText) getChildView(todo6, R.id.todo_text);
                //todo firestore에 값 넣기
            case 5:
                editText = (EditText) getChildView(todo5, R.id.todo_text);
                //todo firestore에 값 넣기
            case 4:
                editText = (EditText) getChildView(todo4, R.id.todo_text);
                //todo firestore에 값 넣기
            case 3:
                editText = (EditText) getChildView(todo3, R.id.todo_text);
                //todo firestore에 값 넣기
            case 2:
                editText = (EditText) getChildView(todo2, R.id.todo_text);
                //todo firestore에 값 넣기
            case 1:
                editText = (EditText) getChildView(todo1, R.id.todo_text);
                //todo firestore에 값 넣기
        }
    }

    // TODOLIST 생성
    public void addTODO() {
        if (todoCount == 6) {
            Snackbar.make(calenderLayout, R.string.할일추가제한, Snackbar.LENGTH_SHORT).show();
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
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    // TODO 해당 유저의 알람 데이터 DB에서 가져오기
    private void updateUI(FirebaseUser user) {

    }

    // firebase-firestore 관리 함수
    // 데이터 저장 형식
    // user > 유저 UID > main (Map<String, Object>를 저장하는) 배열 필드 > Map<String, Object>
    //      Map<String, Object>는 4개의 데이터 저장
    //      1. "create" (타임스탬프) 해당 내용이 생성된 시간
    //      2. "end" (타임스탬프) 알람이 울릴 시간
    //      3. "set" (타임스탬프) 알람이 꺼진 시간 -> 나중에 추가됨(맞춘 알람 시간으로 찾아감)
    //      4. "todo" (배열) 투두리스트에 작성한 내용

    // 저장할 객체 형식을 생성해봅시다
    private void makeStruct() {
        List main = new ArrayList<Object>();
        Map<String, Object> tmp = new HashMap<String, Object>();

        // Update the timestamp field with the value from the server
        Map<String,Object> updates = new HashMap<String, Object>();
        updates.put("timestamp", FieldValue.serverTimestamp());

        tmp.put("create", ""); //타임스탬프 넣기
        tmp.put("end", ""); //타임스탬프 넣기
        tmp.put("set", ""); //타임스탬프 넣기
        tmp.put("todo", ""); //배열 넣기
        main.add(tmp);
    }

    // 단일 문서 만들기. 문서 처음 생성 시(유저의 첫번째 알람 설정 시)에만 사용
    public void setDocument() {
        List main = new ArrayList<Object>();
        Map<String, Object> tmp = new HashMap<String, Object>();

        // Update the timestamp field with the value from the server
        Map<String,Object> updates = new HashMap<String, Object>();
        updates.put("timestamp", FieldValue.serverTimestamp());

        tmp.put("create", FieldValue.serverTimestamp()); //타임스탬프 넣기
        tmp.put("end", null); //타임스탬프 넣기
        tmp.put("set", ""); //타임스탬프 넣기
        tmp.put("todo", ""); //배열 넣기
        main.add(tmp);

        // 유저 UID로 문서 생성 후 그 안에 해당 유저에 대한 정보 삽입
        // 참고 - https://cloud.google.com/firestore/docs/samples/firestore-data-set-array-operations?hl=ko#firestore_data_set_array_operations-java
        DocumentReference document = db.collection("user").document(user.getUid());
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

        Map<String, Object> data = new HashMap<String, Object>();

        // [START set_with_id]
        db.collection("cities").document("new-city-id").set(data);
        // [END set_with_id]
    }

    public void updateDocumentArray(Map<String, Object> data) {
        // [START update_document_array]
        DocumentReference document = db.collection("user").document(user.getUid());

        // todo 추가, 삭제하는 함수 각각 만들어서 빼기
        // Atomically add a new Map<> to the "main" array field.
        document.update("main", FieldValue.arrayUnion(data));

        // Atomically remove a new Map<> to the "main" array field.
        document.update("main", FieldValue.arrayRemove(data));
        // [END update_document_array]
    }
}