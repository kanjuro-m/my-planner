package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Schedule;

public class ScheduleDAO {
    private final String URL = "jdbc:sqlite:myplanner.db";

    public ScheduleDAO() {
        createTableIfNotExists();
    }

    private Connection getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(URL);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS schedules ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "user_id INTEGER NOT NULL, "
                   + "date TEXT NOT NULL, "
                   + "title TEXT NOT NULL"
                   + ")";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("[ERROR] schedulesテーブルの作成に失敗しました:");
            e.printStackTrace();
        }
    }

    /**
     * 指定された日付およびユーザーIDの予定一覧を取得する
     */
    public List<Schedule> findByDateAndUserId(String date, int userId) {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE date = ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, date);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Schedule s = new Schedule();
                    s.setId(rs.getInt("id"));
                    s.setUserId(rs.getInt("user_id"));
                    s.setDate(rs.getString("date"));
                    s.setTitle(rs.getString("title"));
                    list.add(s);
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] 予定の取得に失敗しました (date: " + date + ", userId: " + userId + "):");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 指定された日付の全ユーザー予定を取得する（互換用）
     */
    public List<Schedule> findByDate(String date) {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE date = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Schedule s = new Schedule();
                    s.setId(rs.getInt("id"));
                    s.setUserId(rs.getInt("user_id"));
                    s.setDate(rs.getString("date"));
                    s.setTitle(rs.getString("title"));
                    list.add(s);
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] 予定の取得に失敗しました (date: " + date + "):");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 予定を新規登録する
     */
    public void insert(Schedule schedule) {
        String sql = "INSERT INTO schedules (user_id, date, title) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, schedule.getUserId());
            stmt.setString(2, schedule.getDate());
            stmt.setString(3, schedule.getTitle());
            stmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("[ERROR] 予定の登録に失敗しました:");
            e.printStackTrace();
        }
    }

    /**
     * 予定を削除する
     */
    public void delete(int id) {
        String sql = "DELETE FROM schedules WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("[ERROR] 予定の削除に失敗しました (id: " + id + "):");
            e.printStackTrace();
        }
    }
}