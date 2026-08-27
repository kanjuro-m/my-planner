package model;

import java.io.Serializable;

public class Habit implements Serializable {
    private int id;
    private String name;
    private boolean completed;

    public Habit() {}

    public Habit(int id, String name, boolean completed) {
        this.id = id;
        this.name = name;
        this.completed = completed;
    }

    public Habit(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    // ゲッターとセッター
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}