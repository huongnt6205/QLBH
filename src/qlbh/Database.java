package qlbh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/qlbh?useSSL=false&serverTimezone=UTC";
    private static final String USER = "hungyen";
    private static final String PASSWORD = "112233";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi kết nối MySQL.", e);
        }
    }
}
