package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Diary;

public class DiaryDAO {

    // データベース接続URL（myplanner.db に統一）
    private static final String JDBC_URL = "jdbc:sqlite:myplanner.db";

    /**
     * コンストラクタ：インスタンス化されたときに自動でテーブルをチェック・作成する
     */
    public DiaryDAO() {
        createTableIfNotExists();
    }

    /**
     * データベース接続を取得するメソッド
     */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLiteドライバが見つかりません", e);
        }
        return DriverManager.getConnection(JDBC_URL);
    }

    /**
     * テーブルが存在しない場合に自動作成するメソッド
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS diary (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "user_id INTEGER NOT NULL, " +
                     "title TEXT NOT NULL, " +
                     "content TEXT NOT NULL, " +
                     "date TEXT NOT NULL)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定されたユーザーIDの日記を全件取得する（日付の新しい順）
     */
    public List<Diary> findByUserId(int userId) {
        List<Diary> diaryList = new ArrayList<>();
        String sql = "SELECT id, user_id, title, content, date FROM diary WHERE user_id = ? ORDER BY date DESC, id DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    String date = rs.getString("date");

                    Diary diary = new Diary(id, userId, title, content, date);
                    diaryList.add(diary);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diaryList;
    }

    /**
     * 日記を全件取得する（日付の新しい順）
     */
    public List<Diary> findAll() {
        List<Diary> diaryList = new ArrayList<>();
        String sql = "SELECT id, user_id, title, content, date FROM diary ORDER BY date DESC, id DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String date = rs.getString("date");

                Diary diary = new Diary(id, userId, title, content, date);
                diaryList.add(diary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diaryList;
    }

    /**
     * 指定されたIDの日記を1件取得する
     */
    public Diary findById(int id) {
        Diary diary = null;
        String sql = "SELECT id, user_id, title, content, date FROM diary WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    String date = rs.getString("date");

                    diary = new Diary(id, userId, title, content, date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diary;
    }

    /**
     * 指定された年月・ユーザーIDに日記が存在する日付のリスト（yyyy-MM-dd）を取得する
     */
    public List<String> getDiaryDatesInMonth(int year, int month, int userId) {
        List<String> dates = new ArrayList<>();
        String monthStr = String.format("%04d-%02d-%%", year, month);
        String sql = "SELECT DISTINCT date FROM diary WHERE date LIKE ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, monthStr);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dates.add(rs.getString("date"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }

    /**
     * 新しい日記を追加する
     */
    public boolean create(Diary diary) {
        String sql = "INSERT INTO diary (user_id, title, content, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, diary.getUserId());
            pstmt.setString(2, diary.getTitle());
            pstmt.setString(3, diary.getContent());
            pstmt.setString(4, diary.getDate());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 日記を更新する
     */
    public boolean update(Diary diary) {
        String sql = "UPDATE diary SET title = ?, content = ?, date = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, diary.getTitle());
            pstmt.setString(2, diary.getContent());
            pstmt.setString(3, diary.getDate());
            pstmt.setInt(4, diary.getId());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 日記を削除する
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM diary WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}