package ultrasclaimprotection.database;

import java.sql.*;

import ultrasclaimprotection.UltrasClaimProtection;
import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.console.Console;

public class Database {
    public static UltrasClaimProtection plugin = UltrasClaimProtection.getPlugin(UltrasClaimProtection.class);

    private final String path;
    private Connection connection;

    public Database(String path) {
        this.path = path;
    }

    public Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + this.path);
        this.connection = connection;

        Console.info("Successfully connected to the database.");

        return connection;
    }

    public void closeConnection() throws SQLException {
        if (this.connection != null && !connection.isClosed()) {
            this.connection.close();
        }
    }

    public void getTablesReady() throws SQLException {
        executeStatement("CREATE TABLE IF NOT EXISTS lands (" +
                "land_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "land_name TEXT NOT NULL, " +
                "land_description TEXT NOT NULL, " +
                "owner_uuid TEXT NOT NULL, " +
                "location_x REAL NOT NULL, " +
                "location_y REAL NOT NULL, " +
                "location_z REAL NOT NULL, " +
                "location_world TEXT NOT NULL, " +
                "location_yaw REAL NOT NULL, " +
                "natural_flags INTEGER NOT NULL," +
                "created_at BIGINT NOT NULL)");

        executeStatement("CREATE TABLE IF NOT EXISTS land_chunks (" +
                "chunk_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "chunk_x INTEGER NOT NULL," +
                "chunk_z INTEGER NOT NULL," +
                "chunk_world TEXT NOT NULL, " +
                "land_id INTEGER NOT NULL," +
                "created_at BIGINT NOT NULL)");

        executeStatement("CREATE TABLE IF NOT EXISTS land_roles (" +
                "role_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "role_name TEXT NOT NULL," +
                "role_priority INTEGER NOT NULL, " +
                "land_id INTEGER NOT NULL, " +
                "role_flags INTEGER NOT NULL, "+
                "created_at BIGINT NOT NULL)");
        
        executeStatement("CREATE TABLE IF NOT EXISTS land_members (" +
                "member_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_uuid TEXT NOT NULL, " +
                "land_id INTEGER NOT NULL, " +
                "role_id INTEGER NOT NULL, "+
                "created_at BIGINT NOT NULL)");

        LandsManager.updateCache();
        LandChunksManager.updateCache();
    }

    private void executeStatement(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();

        statement.execute(sql);
        statement.close();
    }
}
