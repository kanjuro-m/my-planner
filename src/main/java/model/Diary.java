package model;

import java.io.Serializable;

public class Diary implements Serializable {
    private int id;         // 日記ID
    private int userId;     // ユーザーID（追加）
    private String title;
    private String content;
    private String date;

    public Diary() {}

    // 全フィールド用コンストラクタ（DB読み出し用）
    public Diary(int id, int userId, String title, String content, String date) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    // IDなしコンストラクタ（新規登録用）
    public Diary(int userId, String title, String content, String date) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    // ゲッターとセッター
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}