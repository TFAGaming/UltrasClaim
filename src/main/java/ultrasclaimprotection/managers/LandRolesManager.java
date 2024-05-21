package ultrasclaimprotection.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ultrasclaimprotection.UltrasClaimProtection;

public class LandRolesManager {
    private static final Map<String, List<Object>> cache = new HashMap<>();

    public static void updateCache() {
        String sql = "SELECT * FROM land_roles";

        try {
            if (!cache.isEmpty()) {
                cache.clear();
            }

            Connection connection = UltrasClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int role_id = result.getInt("role_id");
                String role_name = result.getString("role_name");
                int role_priority = result.getInt("role_priority");
                int land_id = result.getInt("land_id");
                int role_flags = result.getInt("role_flags");
                long created_at = result.getLong("created_at");

                List<Object> cache_data = new ArrayList<Object>();

                cache_data.add(role_id);
                cache_data.add(role_name);
                cache_data.add(role_priority);
                cache_data.add(land_id);
                cache_data.add(role_flags);
                cache_data.add(created_at);

                cache.put(createCacheKey(land_id, role_id), cache_data);
            }

            statement.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public static void create(int land_id, String role_name, int priority, int flags) {
        String sql = "INSERT INTO land_roles ( " +
                "role_name, " +
                "role_priority, " +
                "land_id, " +
                "role_flags, " +
                "created_at" +
                ") VALUES (?,?,?,?,?)";

        try {
            Connection connection = UltrasClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, role_name);
            statement.setInt(2, priority);
            statement.setInt(3, land_id);
            statement.setInt(4, flags);
            statement.setLong(5, System.currentTimeMillis());

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int land_id, int role_id) {
        String sql = "DELETE FROM land_roles WHERE land_id = ? AND role_id = ?";

        try {
            Connection connection = UltrasClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, land_id);
            statement.setInt(2, role_id);

            statement.execute();
            statement.close();

            cache.remove(createCacheKey(land_id, role_id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll(int land_id) {
        String sql = "DELETE FROM land_roles WHERE land_id = ?";

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

    public static boolean containsByID(int land_id, int role_id) {
        boolean value = false;

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if ((int) data.get(0) == role_id && (int) data.get(3) == land_id) {
                value = true;
                break;
            }
        }

        return value;
    }

    public static boolean containsByRoleName(int land_id, String role_name, boolean ignorecase) {
        boolean value = false;

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (ignorecase) {
                if (((String) data.get(1)).equalsIgnoreCase(role_name) && (int) data.get(3) == land_id) {
                    value = true;
                    break;
                }
            } else {
                if (((String) data.get(1)).equals(role_name) && (int) data.get(3) == land_id) {
                    value = true;
                    break;
                }
            }
        }

        return value;
    }

    public static Object get(int land_id, int role_id, String variable) {
        if (cache.containsKey(createCacheKey(land_id, role_id))) {
            List<Object> data = cache.get(createCacheKey(land_id, role_id));

            switch (variable) {
                case "role_id":
                    return data.get(0);
                case "role_name":
                    return data.get(1);
                case "role_priority":
                    return data.get(2);
                case "land_id":
                    return data.get(3);
                case "role_flags":
                    return data.get(4);
                case "created_at":
                    return data.get(5);
                default:
                    return null;
            }
        }

        return null;
    }

    public static List<String> getRoles(int land_id) {
        List<String> arraylist = new ArrayList<String>();

        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if ((int) data.get(3) == land_id) {
                arraylist.add((String) data.get(1));
            }
        }

        return arraylist;
    }

    public static Object getByRoleName(int land_id, String role_name, String variable) {
        for (Map.Entry<String, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(1)).equals(role_name) && (int) data.get(3) == land_id) {
                switch (variable) {
                    case "role_id":
                        return data.get(0);
                    case "role_name":
                        return data.get(1);
                    case "role_priority":
                        return data.get(2);
                    case "land_id":
                        return data.get(3);
                    case "role_flags":
                        return data.get(4);
                    case "created_at":
                        return data.get(5);
                    default:
                        return null;
                }
            }
        }

        return null;
    }

    public static Map<String, List<Object>> getCache() {
        return cache;
    }

    public static void rename(int land_id, int role_id, String name) {
        String sql = "UPDATE land_roles SET role_name='" + name + "' WHERE role_id = ? AND land_id = ?";

        try {
            Connection connection = UltrasClaimProtection.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, role_id);
            statement.setInt(2, land_id);

            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String createCacheKey(int land_id, int role_id) {
        return land_id + "," + role_id;
    }
}
