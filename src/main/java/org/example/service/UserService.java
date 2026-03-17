package org.example.service;


import org.example.dao.UserDao;
import org.example.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public int registerUser(User user) throws SQLException {
        validateUser(user);
        return userDao.insert(user);
    }

    public User getUserById(int id) throws SQLException {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDao.findAll();
    }

    public boolean updateUser(int id, String fullName, String status) throws SQLException {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (!status.equals("ACTIVE") && !status.equals("BLOCKED")) {
            throw new IllegalArgumentException("El estado debe ser ACTIVE o BLOCKED.");
        }
        return userDao.update(id, fullName, status);
    }

    public boolean deleteUser(int id) throws SQLException {
        return userDao.delete(id);
    }

    private void validateUser(User user) {
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException("La contraseña (hash) no puede estar vacía.");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("CLIENT");
        }
        if (user.getStatus() == null || user.getStatus().isBlank()) {
            user.setStatus("ACTIVE");
        }
    }
}