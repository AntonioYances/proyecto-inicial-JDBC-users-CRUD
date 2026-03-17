package org.example.app;

import org.example.model.User;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            printMenu();
            String option = scanner.nextLine().trim();

            try {
                switch (option) {
                    case "1" -> createUser(scanner);
                    case "2" -> listUsers();
                    case "3" -> findUserById(scanner);
                    case "4" -> updateUser(scanner);
                    case "5" -> deleteUser(scanner);
                    case "0" -> isRunning = false;
                    default -> System.out.println("Opción inválida. Intenta de nuevo.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Validación: " + e.getMessage());
            }
        }

        System.out.println("Programa finalizado.");
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== SALOPLAY JDBC - MÓDULO USERS (CRUD) ===");
        System.out.println("1. Insertar usuario");
        System.out.println("2. Listar usuarios");
        System.out.println("3. Consultar usuario por ID");
        System.out.println("4. Actualizar usuario (nombre y estado)");
        System.out.println("5. Eliminar usuario");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opción: ");
    }

    private static void createUser(Scanner scanner) throws SQLException {
        System.out.print("Nombre completo: ");
        String fullName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password hash (texto cualquiera para prueba): ");
        String passwordHash = scanner.nextLine();

        User user = new User(fullName, email, passwordHash, "CLIENT", "ACTIVE");
        int generatedId = userService.registerUser(user);

        System.out.println("✅ Usuario creado con ID: " + generatedId);
    }

    private static void listUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        users.forEach(System.out::println);
    }

    private static void findUserById(Scanner scanner) throws SQLException {
        System.out.print("ID del usuario: ");
        int id = Integer.parseInt(scanner.nextLine());

        User user = userService.getUserById(id);
        if (user == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        System.out.println(user);
    }

    private static void updateUser(Scanner scanner) throws SQLException {
        System.out.print("ID del usuario a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Nuevo nombre completo: ");
        String fullName = scanner.nextLine();

        System.out.print("Nuevo estado (ACTIVE/BLOCKED): ");
        String status = scanner.nextLine().trim().toUpperCase();

        boolean updated = userService.updateUser(id, fullName, status);
        System.out.println(updated ? "✅ Usuario actualizado." : "Usuario no encontrado.");
    }

    private static void deleteUser(Scanner scanner) throws SQLException {
        System.out.print("ID del usuario a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        boolean deleted = userService.deleteUser(id);
        System.out.println(deleted ? "✅ Usuario eliminado." : "Usuario no encontrado.");
    }
}