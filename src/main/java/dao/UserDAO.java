package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;

public class UserDAO {
    private final String JDBC_URL = "jdbc:sqlite:C:/db/journey_hub.db";

    public UserDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
            createTableIfNotExists();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "username TEXT NOT NULL UNIQUE, "
                   + "password TEXT NOT NULL, "
                   + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ★ 50行目のエラー解決に必要なメソッド
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ★ 新規登録処理
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}