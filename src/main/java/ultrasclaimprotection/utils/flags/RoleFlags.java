package ultrasclaimprotection.utils.flags;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class RoleFlags {
    public static final int BREAK_BLOCKS = 1;
    public static final int PLACE_BLOCKS = 1 << 1;
    public static final int CONTAINERS = 1 << 2;
    public static final int DOORS = 1 << 3;
    public static final int TRAP_DOORS = 1 << 4;
    public static final int FENCE_GATES = 1 << 5;
    public static final int SIGNS = 1 << 6;
    public static final int REDSTONE = 1 << 7;
    public static final int LEVERS = 1 << 8;
    public static final int BUTTONS = 1 << 9;
    public static final int PRESSURE_PLATES = 1 << 10;
    public static final int FROST_WALKER = 1 << 11;
    public static final int HARVEST_CROPS = 1 << 12;
    public static final int GENERAL_INTERACTION = 1 << 13;
    public static final int ARMOR_STANDS = 1 << 14;
    public static final int INTERACT_ENTITIES = 1 << 15;
    public static final int DAMAGE_PASSIVE_ENTITIES = 1 << 16;
    public static final int DAMAGE_HOSTILE_ENTITIES = 1 << 17;
    public static final int TRADE_VILLAGERS = 1 << 18;
    public static final int IGNITE = 1 << 19;
    public static final int VEHICLES = 1 << 20;
    public static final int TELEPORT_SPAWN = 1 << 21;
    public static final int ENTER_LAND = 1 << 22;
    public static final int PVP = 1 << 23;
    public static final int THROW_ENDER_PEARLS = 1 << 24;
    public static final int THROW_POTIONS = 1 << 25;
    public static final int PICKUP_ITEMS = 1 << 26;

    public static List<String> listPermissions() {
        return Lists.newArrayList(
                "BREAK_BLOCKS",
                "PLACE_BLOCKS",
                "CONTAINERS",
                "DOORS",
                "TRAP_DOORS",
                "FENCE_GATES",
                "SIGNS",
                "REDSTONE",
                "LEVERS",
                "BUTTONS",
                "PRESSURE_PLATES",
                "FROST_WALKER",
                "HARVEST_CROPS",
                "GENERAL_INTERACTION",
                "ARMOR_STANDS",
                "INTERACT_ENTITIES",
                "DAMAGE_PASSIVE_ENTITIES",
                "DAMAGE_HOSTILE_ENTITIES",
                "TRADE_VILLAGERS",
                "IGNITE",
                "VEHICLES",
                "TELEPORT_SPAWN",
                "ENTER_LAND",
                "PVP",
                "THROW_ENDER_PEARLS",
                "THROW_POTIONS",
                "PICKUP_ITEMS");
    }

    public static int valueOf(String name) {
        List<String> allpermissions = listPermissions();

        if (allpermissions.indexOf(name) == 0) {
            return 1;
        } else {
            return 1 << (allpermissions.indexOf(name));
        }
    }

    public static String from(int flag) {
        List<String> allpermissions = listPermissions();

        for (int i = 0; i < allpermissions.size(); i++) {
            int value = valueOf(allpermissions.get(i));

            if (value == flag) {
                return allpermissions.get(i);
            }
        }

        return "UNKNOWN";
    }

    public static List<List<Object>> getAsList(int flags) {
        List<String> allpermissions = listPermissions();
        List<List<Object>> data = new ArrayList<List<Object>>();

        for (String permission : allpermissions) {
            int value = valueOf(permission);

            data.add(Lists.newArrayList(permission, FlagsCalculator.isFlagSet(flags, value)));
        }

        return data;
    }
}
