
package app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static String url;

    public static void init(String dbPath) {
        url = "jdbc:sqlite:" + dbPath;
        try (Connection conn = get();
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS decks (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS cards (id INTEGER PRIMARY KEY AUTOINCREMENT, deck_id INTEGER NOT NULL, question TEXT NOT NULL, answer TEXT NOT NULL, FOREIGN KEY(deck_id) REFERENCES decks(id) ON DELETE CASCADE)");
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed: " + e.getMessage(), e);
        }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
