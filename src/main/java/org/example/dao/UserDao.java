package org.example.dao;


import org.example.config.DatabaseConfig;
import org.example.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public int insert(User user) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, password_hash, role, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar el usuario.");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("No se pudo obtener el ID generado.");
        }
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT id, full_name, email, password_hash, role, status, created_at FROM users WHERE id = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
                return null;
            }
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, full_name, email, password_hash, role, status, created_at FROM users ORDER BY id";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
            return users;
        }
    }

    public boolean update(int id, String fullName, String status) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, status = ? WHERE id = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, fullName);
            statement.setString(2, status);
            statement.setInt(3, id);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String fullName = rs.getString("full_name");
        String email = rs.getString("email");
        String passwordHash = rs.getString("password_hash");
        String role = rs.getString("role");
        String status = rs.getString("status");

        Timestamp createdAtTs = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdAtTs != null ? createdAtTs.toLocalDateTime() : null;

        return new User(id, fullName, email, passwordHash, role, status, createdAt);
    }

}
