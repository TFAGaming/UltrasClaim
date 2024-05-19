package ultrasclaimprotection.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import ultrasclaimprotection.UltrasClaimProtection;

public class LandChunksManager {
    private static final Map<String, List<Object>> cache = new HashMap<>();

    public static void updateCache() {
        String sql = "SELECT * FROM land_chunks";

        try {
            if (!cache.isEmpty()) {
                cache.clear();
            }

            Connection connection = UltrasClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int chunk_id = result.getInt("chunk_id");
                int chunk_x = result.getInt("chunk_x");
                int chunk_z = result.getInt("chunk_z");
                String chunk_world = result.getString("chunk_world");
                int land_id = result.getInt("land_id");
                double created_at = result.getLong("created_at");

                List<Object> cache_data = new ArrayList<Object>();

                cache_data.add(chunk_id);
                cache_data.add(chunk_x);
                cache_data.add(chunk_z);
                cache_data.add(chunk_world);
                cache_data.add(land_id);
                cache_data.add(created_at);

                cache.put(createCacheKey(chunk_x, chunk_z, chunk_world), cache_data);
            }

            statement.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public static void create(int land_id, Chunk chunk) {
        String sql = "INSERT INTO land_chunks ( " +
                "chunk_x, " +
                "chunk_z, " +
                "chunk_world, " +
                "land_id, " +
                "created_at" +
                ") VALUES (?,?,?,?,?)";

        try {
            Connection connection = UltrasClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, chunk.getX());
            statement.setInt(2, chunk.getZ());
            statement.setString(3, chunk.getWorld().getName());
            statement.setInt(4, land_id);
            statement.setLong(5, System.currentTimeMillis());

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Chunk chunk) {
        String sql = "DELETE FROM land_chunks WHERE chunk_x = ? AND chunk_z = ? AND chunk_world = ?";

        try {
            Connection connection = UltrasClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, chunk.getX());
            statement.setInt(2, chunk.getZ());
            statement.setString(3, chunk.getWorld().getName());

            statement.executeUpdate();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll(int land_id) {
        String sql = "DELETE FROM land_chunks WHERE land_id = ?";

        try {
            Connection connection = UltrasClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, land_id);

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean contains(Chunk chunk) {
        boolean value = false;

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();
            int chunk_x = (int) data.get(1);
            int chunk_z = (int) data.get(2);
            String chunk_world = (String) data.get(3);

            if (chunk_x == chunk.getX() && chunk_z == chunk.getZ() && chunk_world.equals(chunk.getWorld().getName())) {
                value = true;
                break;
            }
        }

        return value;
    }

    public static Object get(Chunk chunk, String variable) {
        if (cache.containsKey(createCacheKeyByChunk(chunk))) {
            List<Object> data = cache.get(createCacheKeyByChunk(chunk));

            switch (variable) {
                case "chunk_id":
                    return data.get(0);
                case "chunk_x":
                    return data.get(1);
                case "chunk_z":
                    return data.get(2);
                case "chunk_world":
                    return data.get(3);
                case "land_id":
                    return data.get(4);
                case "created_at":
                    return data.get(5);
                default:
                    return null;
            }
        }

        return null;
    }

    public static Map<String, List<Object>> getCache() {
        return cache;
    }

    public static int count(int land_id) {
        int count = 0;

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((int) data.get(4)) == land_id) {
                count++;
            }
        }

        return count;
    };

    public static Player getChunkOwner(Chunk chunk) {
        if (contains(chunk)) {
            int land_id = (int) get(chunk, "land_id");

            return Bukkit.getOfflinePlayer(UUID.fromString((String) LandsManager.get(land_id, "owner_uuid"))).getPlayer();
        } else {
            return null;
        }
    }

    private static String createCacheKey(int chunk_x, int chunk_z, String chunk_world) {
        return chunk_x + "," + chunk_z + "," + chunk_world;
    }

    private static String createCacheKeyByChunk(Chunk chunk) {
        return chunk.getX() + "," + chunk.getZ() + "," + chunk.getWorld().getName();
    }
}