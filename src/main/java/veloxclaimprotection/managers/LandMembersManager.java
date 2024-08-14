package veloxclaimprotection.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import veloxclaimprotection.VeloxClaimProtection;

public class LandMembersManager {
    private static final Map<String, List<Object>> cache = new HashMap<>();

    public static void updateCache() {
        String sql = "SELECT * FROM land_members";

        try {
            if (!cache.isEmpty()) {
                cache.clear();
            }

            Connection connection = VeloxClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int member_id = result.getInt("member_id");
                String member_uuid = result.getString("member_uuid");
                int land_id = result.getInt("land_id");
                int role_id = result.getInt("role_id");
                long created_at = result.getLong("created_at");

                List<Object> cache_data = new ArrayList<Object>();

                cache_data.add(member_id);
                cache_data.add(member_uuid);
                cache_data.add(land_id);
                cache_data.add(role_id);
                cache_data.add(created_at);

                cache.put(createCacheKey(land_id, member_id), cache_data);
            }

            statement.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public static void create(int land_id, OfflinePlayer player, int role_id) {
        String sql = "INSERT INTO land_members ( " +
                "member_uuid, " +
                "land_id, " +
                "role_id, " +
                "created_at" +
                ") VALUES (?,?,?,?)";

        try {
            Connection connection = VeloxClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, player.getUniqueId().toString());
            statement.setInt(2, land_id);
            statement.setInt(3, role_id);
            statement.setLong(4, System.currentTimeMillis());

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int land_id, OfflinePlayer player) {
        String sql = "DELETE FROM land_members WHERE land_id = ? AND member_uuid = ?";

        try {
            Connection connection = VeloxClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, land_id);
            statement.setString(2, player.getUniqueId().toString());

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll(int land_id) {
        String sql = "DELETE FROM land_members WHERE land_id = ?";

        try {
            Connection connection = VeloxClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, land_id);

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean contains(int land_id, OfflinePlayer player) {
        boolean value = false;

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(1)).equals(player.getUniqueId().toString()) && (int) data.get(2) == land_id) {
                value = true;
                break;
            }
        }

        return value;
    }

    public static Object get(int land_id, int member_id, String variable) {
        if (cache.containsKey(createCacheKey(land_id, member_id))) {
            List<Object> data = cache.get(createCacheKey(land_id, member_id));

            switch (variable) {
                case "member_id":
                    return data.get(0);
                case "member_uuid":
                    return data.get(1);
                case "land_id":
                    return data.get(2);
                case "role_id":
                    return data.get(3);
                case "created_at":
                    return data.get(4);
                default:
                    return null;
            }
        }

        return null;
    }

    public static Object getByPlayer(int land_id, Player player, String variable) {
        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(1)).equals(player.getUniqueId().toString()) && (int) data.get(2) == land_id) {
                switch (variable) {
                    case "member_id":
                        return data.get(0);
                    case "member_uuid":
                        return data.get(1);
                    case "land_id":
                        return data.get(2);
                    case "role_id":
                        return data.get(3);
                    case "created_at":
                        return data.get(4);
                    default:
                        return null;
                }
            }
        }

        return null;
    }

    public static List<List<Object>> getListLandMembers(int land_id) {
        List<List<Object>> arraylist = new ArrayList<List<Object>>();

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if ((int) data.get(2) == land_id) {
                List<Object> sub_arraylist = new ArrayList<Object>();

                sub_arraylist.add(data.get(1));
                sub_arraylist.add(data.get(3));
                sub_arraylist.add(data.get(4));

                arraylist.add(sub_arraylist);
            }
        }

        return arraylist;
    }

    public static Map<String, List<Object>> getCache() {
        return cache;
    }

    public static List<String> getPlayerLandsList(OfflinePlayer player) {
        List<String> lands = new ArrayList<>();

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(1)).equals(player.getUniqueId().toString())) {
                lands.add((String) LandsManager.get((int) data.get(2), "land_name"));
            }
        }

        return lands;
    }

    public static void updatePlayerPosition(int land_id, OfflinePlayer player, int role_id) {
        String sql = "UPDATE land_members SET role_id=" + role_id + " WHERE member_uuid = ? AND land_id = ?";

        try {
            Connection connection = VeloxClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, player.getUniqueId().toString());
            statement.setInt(2, land_id);

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int count(int land_id) {
        int count = 0;

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((int) data.get(2)) == land_id) {
                count++;
            }
        }

        return count;
    }

    public static int countByPlayer(OfflinePlayer player) {
        int count = 0;

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(1)).equals(player.getUniqueId().toString())) {
                count++;
            }
        }

        return count;
    }

    private static String createCacheKey(int land_id, int member_id) {
        return land_id + "," + member_id;
    }
}
