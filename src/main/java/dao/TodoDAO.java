package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Todo;

public class TodoDAO {
    private final String JDBC_URL = "jdbc:sqlite:C:/db/journey_hub.db";

    public TodoDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
            createTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    // テーブル作成（user_id カラムを追加）
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS todos ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "user_id INTEGER NOT NULL, " // ★ ユーザーID
                   + "title TEXT NOT NULL, "
                   + "is_completed INTEGER DEFAULT 0, "
                   + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP"
                   + ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 特定のユーザーのTODOのみ取得
    public List<Todo> getTodosByUserId(int userId) {
        List<Todo> todos = new ArrayList<>();
        String sql = "SELECT * FROM todos WHERE user_id = ? ORDER BY id DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Todo todo = new Todo();
                    todo.setId(rs.getInt("id"));
                    todo.setUserId(rs.getInt("user_id"));
                    todo.setTitle(rs.getString("title"));
                    todo.setCompleted(rs.getInt("is_completed") == 1);
                    todo.setCreatedAt(rs.getString("created_at"));
                    todos.add(todo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    // TODOの追加（user_id も一緒に保存）
    public boolean addTodo(int userId, String title) {
        String sql = "INSERT INTO todos (user_id, title) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, title);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // TODOの削除
    public boolean deleteTodo(int id, int userId) {
        String sql = "DELETE FROM todos WHERE id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ★ 追加：TODOの完了/未完了の切替（toggle）
    public boolean toggleTodo(int id, int userId) {
        String sql = "UPDATE todos SET is_completed = CASE WHEN is_completed = 1 THEN 0 ELSE 1 END WHERE id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}