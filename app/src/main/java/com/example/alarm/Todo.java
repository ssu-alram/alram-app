package com.example.alarm;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Todo {
    private Timestamp createdTime;
    private Timestamp setTime;
    private Timestamp endTime;
    private ArrayList<String> todoData;

    public Todo() {
        this.createdTime = null;
        this.setTime = null;
        this.endTime = null;
        this.todoData = null;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public Timestamp getSetTime() {
        return setTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public ArrayList<String> getTodoData() {
        return todoData;
    }

    public Boolean setCreatedTime(Timestamp create) {
        this.createdTime = create;
        return true;
    }

    public Boolean setSetTime(Timestamp set) {
        this.setTime = set;
        return true;
    }

    public Boolean setEndTime(Timestamp end) {
        this.endTime = end;
        return true;
    }

    public Boolean setTodoData(ArrayList<String> todo) {
        this.todoData = todo;
        return true;
    }
}
