package veloxclaimprotection.managers;

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
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import veloxclaimprotection.VeloxClaimProtection;
import veloxclaimprotection.utils.flags.FlagsCalculator;

public class LandsManager {
    private static final Map<Integer, List<Object>> cache = new HashMap<>();

    public static void updateCache() {
        String sql = "SELECT * FROM lands";

        try {
            if (!cache.isEmpty()) {
                cache.clear();
            }

            Connection connection = VeloxClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int land_id = result.getInt("land_id");
                String land_name = result.getString("land_name");
                String land_description = result.getString("land_description");
                String owner_uuid = result.getString("owner_uuid");
                double location_x = result.getDouble("location_x");
                double location_y = result.getDouble("location_y");
                double location_z = result.getDouble("location_z");
                String location_world = result.getString("location_world");
                float location_yaw = result.getFloat("location_yaw");
                int natural_flags = result.getInt("natural_flags");
                long created_at = result.getLong("created_at");

                List<Object> cache_data = new ArrayList<Object>();

                cache_data.add(land_id);
                cache_data.add(land_name);
                cache_data.add(land_description);
                cache_data.add(owner_uuid);
                cache_data.add(location_x);
                cache_data.add(location_y);
                cache_data.add(location_z);
                cache_data.add(location_world);
                cache_data.add(location_yaw);
                cache_data.add(natural_flags);
                cache_data.add(created_at);

                cache.put(land_id, cache_data);
            }

            statement.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public static void create(String land_name, Player player, int flags) {
        String sql = "INSERT INTO lands ( " +
                "land_name, " +
                "land_description, " +
                "owner_uuid, " +
                "location_x, " +
                "location_y, " +
                "location_z, " +
                "location_world, " +
                "location_yaw, " +
                "natural_flags, " +
                "created_at" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            Connection connection = VeloxClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, land_name);
            statement.setString(2, VeloxClaimProtection.getPlugin(VeloxClaimProtection.class).getConfig().getString("lands.default_description").replace("%player_name%", player.getName()));
            statement.setString(3, player.getUniqueId().toString());
            statement.setDouble(4, player.getLocation().getX());
            statement.setDouble(5, player.getLocation().getY());
            statement.setDouble(6, player.getLocation().getZ());
            statement.setString(7, player.getLocation().getWorld().getName());
            statement.setDouble(8, player.getLocation().getYaw());
            statement.setInt(9, flags);
            statement.setLong(10, System.currentTimeMillis());

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int land_id) {
        String sql = "DELETE FROM lands WHERE land_id = ?";

        try {
            Connection connection = VeloxClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, land_id);

            statement.execute();
            statement.close();

            cache.remove(land_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rename(int land_id, String name) {
        String sql = "UPDATE lands SET land_name='" + name + "' WHERE land_id = ?";

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

    public static boolean containsPlayer(Player player) {
        boolean value = false;

        for (Map.Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(3)).equals(player.getUniqueId().toString())) {
                value = true;
                break;
            }
        }

        return value;
    }

    public static boolean containsLandName(String land_name) {
        boolean value = false;

        for (Map.Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(1)).equalsIgnoreCase(land_name)) {
                value = true;
                break;
            }
        }

        return value;
    }

    public static Object get(int land_id, String variable) {
        if (cache.containsKey(land_id)) {
            List<Object> data = cache.get(land_id);

            switch (variable) {
                case "land_id":
                    return data.get(0);
                case "land_name":
                    return data.get(1);
                case "land_description":
                    return data.get(2);
                case "owner_uuid":
                    return data.get(3);
                case "location_x":
                    return data.get(4);
                case "location_y":
                    return data.get(5);
                case "location_z":
                    return data.get(6);
                case "location_world":
                    return data.get(7);
                case "location_yaw":
                    return data.get(8);
                case "natural_flags":
                    return data.get(9);
                case "created_at":
                    return data.get(10);
                default:
                    return null;
            }
        }

        return null;
    }

    public static Object getByPlayer(OfflinePlayer player, String variable) {
        for (Map.Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(3)).equals(player.getUniqueId().toString())) {
                switch (variable) {
                    case "land_id":
                        return data.get(0);
                    case "land_name":
                        return data.get(1);
                    case "land_description":
                        return data.get(2);
                    case "owner_uuid":
                        return data.get(3);
                    case "location_x":
                        return data.get(4);
                    case "location_y":
                        return data.get(5);
                    case "location_z":
                        return data.get(6);
                    case "location_world":
                        return data.get(7);
                    case "location_yaw":
                        return data.get(8);
                    case "natural_flags":
                        return data.get(9);
                    case "created_at":
                        return data.get(10);
                    default:
                        return null;
                }
            }
        }

        return null;
    }

    public static Object getByLandName(String land_name, String variable) {
        for (Map.Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(1)).equalsIgnoreCase(land_name)) {
                switch (variable) {
                    case "land_id":
                        return data.get(0);
                    case "land_name":
                        return data.get(1);
                    case "land_description":
                        return data.get(2);
                    case "owner_uuid":
                        return data.get(3);
                    case "location_x":
                        return data.get(4);
                    case "location_y":
                        return data.get(5);
                    case "location_z":
                        return data.get(6);
                    case "location_world":
                        return data.get(7);
                    case "location_yaw":
                        return data.get(8);
                    case "natural_flags":
                        return data.get(9);
                    case "created_at":
                        return data.get(10);
                    default:
                        return null;
                }
            }
        }

        return null;
    }

    public static Map<Integer, List<Object>> getCache() {
        return cache;
    }

    public static List<String> getPlayerUUIDs() {
        List<String> player_uuids = new ArrayList<>();

        for (Map.Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            player_uuids.add((String) data.get(3));
        }

        return player_uuids;
    }

    public static List<String> getPlayerNames() {
        List<String> player_names = new ArrayList<>();

        for (Map.Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            player_names.add(Bukkit.getOfflinePlayer(UUID.fromString((String) data.get(3))).getName());
        }

        return player_names;
    }

    public static List<String> getListLandNames() {
        List<String> land_names = new ArrayList<>();

        for (Map.Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            land_names.add((String) data.get(1));
        }

        return land_names;
    }

    public static void updateDescription(int land_id, String description) {
        String sql = "UPDATE lands SET land_description='" + description + "' WHERE land_id = ?";

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

    public static void updateLocation(int land_id, Player player) {
        String sql = "UPDATE lands SET location_x=" + player.getLocation().getX() + ", location_y=" + player.getLocation().getY() + ", location_z="
                + player.getLocation().getZ() + ", location_world = '" + player.getLocation().getWorld().getName() + "', location_yaw=" + player.getLocation().getYaw()
                + " WHERE land_id = ?";

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

    public static Location getLocation(int land_id) {
        double location_x = (double) get(land_id, "location_x");
        double location_y = (double) get(land_id, "location_y");
        double location_z = (double) get(land_id, "location_z");
        String location_world = (String) get(land_id, "location_world");
        float location_yaw = (float) get(land_id, "location_yaw");

        Location location = new Location(Bukkit.getWorld(location_world), location_x, location_y, location_z);

        location.setYaw(location_yaw);

        return location;
    }

    public static void updateNaturalFlags(int land_id, int flags) {
        String sql = "UPDATE lands SET natural_flags=" + flags + " WHERE land_id = ?";

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

        return;
    }

    public static boolean isFlagSet(int land_id, int flag) {
        int flags = (int) get(land_id, "natural_flags");

        return FlagsCalculator.isFlagSet(flags, flag);
    }
}
