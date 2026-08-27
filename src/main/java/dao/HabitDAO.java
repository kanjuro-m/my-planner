package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Habit;

public class HabitDAO {
    private final String JDBC_URL = "jdbc:sqlite:planner.db";

    public HabitDAO() {
        // テーブルがなければ自動作成
        String sqlHabit = "CREATE TABLE IF NOT EXISTS habit (" +
                          "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                          "name TEXT NOT NULL)";
                          
        String sqlCheck = "CREATE TABLE IF NOT EXISTS habit_check (" +
                          "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                          "habit_id INTEGER, " +
                          "date TEXT, " +
                          "completed INTEGER DEFAULT 0)";
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(JDBC_URL);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sqlHabit);
                stmt.execute(sqlCheck);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(JDBC_URL);
    }

    // 全件取得メソッド
    public List<Habit> findAll() {
        List<Habit> list = new ArrayList<>();
        String sql = "SELECT id, name FROM habit ORDER BY id DESC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Habit h = new Habit();
                h.setId(rs.getInt("id"));
                h.setName(rs.getString("name"));
                list.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 新規追加メソッド (HabitAddServlet用)
    public boolean create(Habit habit) {
        String sql = "INSERT INTO habit (name) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, habit.getName());
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 削除メソッド (HabitDeleteServlet用)
    public boolean delete(int id) {
        String sqlHabit = "DELETE FROM habit WHERE id = ?";
        String sqlCheck = "DELETE FROM habit_check WHERE habit_id = ?";
        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCheck)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHabit)) {
                pstmt.setInt(1, id);
                int result = pstmt.executeUpdate();
                return result > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 指定日の完了チェック判定
    public boolean isCompletedOnDate(int habitId, String date) {
        String sql = "SELECT completed FROM habit_check WHERE habit_id = ? AND date = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, habitId);
            pstmt.setString(2, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("completed") == 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // チェック状態の保存
    public void saveCheck(int habitId, String date, int completed) {
        String checkSql = "SELECT COUNT(*) FROM habit_check WHERE habit_id = ? AND date = ?";
        String updateSql = "UPDATE habit_check SET completed = ? WHERE habit_id = ? AND date = ?";
        String insertSql = "INSERT INTO habit_check (habit_id, date, completed) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {
            boolean exists = false;
            try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                pstmt.setInt(1, habitId);
                pstmt.setString(2, date);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setInt(1, completed);
                    pstmt.setInt(2, habitId);
                    pstmt.setString(3, date);
                    pstmt.executeUpdate();
                }
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setInt(1, habitId);
                    pstmt.setString(2, date);
                    pstmt.setInt(3, completed);
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}