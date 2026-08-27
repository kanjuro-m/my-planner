package model;

import java.io.Serializable;

public class Todo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId; // ★ ユーザーIDを追加
    private String title;
    private boolean completed;
    private String createdAt;

    public Todo() {}

    public Todo(int id, int userId, String title, boolean completed, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    // ゲッター・セッター
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; } // ★ 追加
    public void setUserId(int userId) { this.userId = userId; } // ★ 追加

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}