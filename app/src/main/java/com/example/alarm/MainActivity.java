package com.example.alarm;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout calenderLayout;
    private LinearLayout mainLayout;
    private LinearLayout container;
    private MaterialCalendarView calendarView;

    private ArrayList<String> items; // 빈 데이터 리스트 생성
    private ArrayAdapter adapter; // ArrayAdapter 생성
    private ListView listview; // listview 생성 및 adapter 지정

    public MainActivity() {
        super(R.layout.activity_main);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calenderLayout = findViewById(R.id.calender);
        mainLayout = findViewById(R.id.main);
//        container = findViewById(R.id.container);

        calenderLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

        findViewById(R.id.tab1).setOnClickListener(this);
        findViewById(R.id.tab2).setOnClickListener(this);
        findViewById(R.id.add_todo).setOnClickListener(this);

        // 출처 - https://recipes4dev.tistory.com/48
        items = new ArrayList<String>();
        // 문제 해결 - https://stackoverflow.com/questions/28192815/how-to-add-customised-layout-to-arrayadapter
        adapter = new ArrayAdapter(this, R.layout.todo_list_design, R.id.textview, items);
        listview = findViewById(R.id.listview);
        listview.setAdapter(adapter);
        addTODO();
        addTODO();
    }



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
        else if (v == R.id.add_todo) addTODO();
    }

    public void addTODO() {
        if(adapter.getCount() == 6)
        {
            Snackbar.make(calenderLayout, R.string.할일추가제한, Snackbar.LENGTH_SHORT).show();
        }
        else {
            // 아이템 추가.
            items.add(getResources().getString(R.string.할일추가));
            // listview 갱신
            adapter.notifyDataSetChanged();
        }
    }
}