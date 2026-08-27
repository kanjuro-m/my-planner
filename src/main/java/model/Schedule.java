package model;

import java.io.Serializable;

public class Schedule implements Serializable {
    private int id;         // 予定ID
    private int userId;     // ユーザーID（追加）
    private String date;    // 日付
    private String title;   // 予定タイトル

    public Schedule() {}

    // 全フィールド用コンストラクタ（DB読み出し用）
    public Schedule(int id, int userId, String date, String title) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.title = title;
    }

    // IDなしコンストラクタ（新規登録用）
    public Schedule(int userId, String date, String title) {
        this.userId = userId;
        this.date = date;
        this.title = title;
    }

    // ゲッターとセッター
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}