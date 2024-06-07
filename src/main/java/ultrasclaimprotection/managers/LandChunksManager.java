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
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import ultrasclaimprotection.UltrasClaimProtection;
import ultrasclaimprotection.utils.flags.PlayerFlags;

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
                long created_at = result.getLong("created_at");

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

    public static Object getByChunkCoordinates(int chunk_x, int chunk_z, String chunk_world, String variable) {
        if (cache.containsKey(createCacheKey(chunk_x, chunk_z, chunk_world))) {
            List<Object> data = cache.get(createCacheKey(chunk_x, chunk_z, chunk_world));

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
    }

    public static OfflinePlayer getChunkOwner(Chunk chunk) {
        if (contains(chunk)) {
            int land_id = (int) get(chunk, "land_id");

            return Bukkit.getOfflinePlayer(UUID.fromString((String) LandsManager.get(land_id, "owner_uuid")));
        } else {
            return null;
        }
    }

    public static List<List<Object>> getChunks(int land_id) {
        List<List<Object>> chunks = new ArrayList<>();

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if ((int) data.get(4) == land_id) {
                chunks.add(data.subList(1, 6));
            }
        }

        return chunks;
    }

    public static int getPlayerFlag(int land_id, Player player) {
        Player chunk_owner = Bukkit.getOfflinePlayer(UUID.fromString((String) LandsManager.get(land_id, "owner_uuid")))
                .getPlayer();

        if (chunk_owner.getUniqueId().equals(player.getUniqueId())) {
            return PlayerFlags.LAND_OWNER;
        }

        return PlayerFlags.LAND_VISITOR;
    }

    public static int getPlayerFlagByChunk(Chunk chunk, Player player) {
        OfflinePlayer chunk_owner = getChunkOwner(chunk);
        int land_id = (int) LandChunksManager.get(chunk, "land_id");

        if (chunk_owner.getUniqueId().equals(player.getUniqueId())) {
            return PlayerFlags.LAND_OWNER;
        } else if (LandMembersManager.contains(land_id, player)) {
            return PlayerFlags.LAND_MEMBER;
        }

        return PlayerFlags.LAND_VISITOR;
    }

    public static boolean findNeighborChunk(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        World world = player.getWorld();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        String chunkWorldName = chunk.getWorld().getName();

        if (!world.getName().equals(chunkWorldName))
            return false;

        Chunk north = world.getChunkAt(chunkX, chunkZ - 1);
        Chunk south = world.getChunkAt(chunkX, chunkZ + 1);
        Chunk west = world.getChunkAt(chunkX - 1, chunkZ);
        Chunk east = world.getChunkAt(chunkX + 1, chunkZ);

        if (contains(north)) {
            OfflinePlayer temp_land_owner = getChunkOwner(north);

            if (!temp_land_owner.getUniqueId().toString().equals(player.getUniqueId().toString()))
                return true;
        }

        if (contains(south)) {
            OfflinePlayer temp_land_owner = getChunkOwner(south);

            if (!temp_land_owner.getUniqueId().toString().equals(player.getUniqueId().toString()))
                return true;
        }

        if (contains(west)) {
            OfflinePlayer temp_land_owner = getChunkOwner(west);

            if (!temp_land_owner.getUniqueId().toString().equals(player.getUniqueId().toString()))
                return true;
        }

        if (contains(east)) {
            OfflinePlayer temp_land_owner = getChunkOwner(east);

            if (!temp_land_owner.getUniqueId().toString().equals(player.getUniqueId().toString()))
                return true;
        }

        return false;
    }

    private static String createCacheKey(int chunk_x, int chunk_z, String chunk_world) {
        return chunk_x + "," + chunk_z + "," + chunk_world;
    }

    private static String createCacheKeyByChunk(Chunk chunk) {
        return chunk.getX() + "," + chunk.getZ() + "," + chunk.getWorld().getName();
    }
}
