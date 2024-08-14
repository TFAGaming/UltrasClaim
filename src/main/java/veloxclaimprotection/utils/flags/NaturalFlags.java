package veloxclaimprotection.utils.flags;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class NaturalFlags {
    public static final int PASSIVE_ENTITIES_SPAWN = 1;
    public static final int HOSTILE_ENTITIES_SPAWN = 1 << 1;
    public static final int ENTITIES_GRIEF = 1 << 2;
    public static final int ENTITIES_DAMAGE_ENTITIES = 1 << 3;
    public static final int LEAVES_DECAY = 1 << 4;
    public static final int FIRE_SPREAD = 1 << 5;
    public static final int LIQUID_FLOW = 1 << 6;
    public static final int EXPLOSIONS_DAMAGE = 1 << 7;
    public static final int WILDERNESS_PISTONS = 1 << 8;
    public static final int WILDERNESS_DISPENSERS = 1 << 9;
    public static final int PLANT_GROWTH = 1 << 10;
    public static final int RAID_TRIGGER = 1 << 11;

    public static List<String> listFlags() {
        return Lists.newArrayList(
                "PASSIVE_ENTITIES_SPAWN",
                "HOSTILE_ENTITIES_SPAWN",
                "ENTITIES_GRIEF",
                "ENTITIES_DAMAGE_ENTITIES",
                "LEAVES_DECAY",
                "FIRE_SPREAD",
                "LIQUID_FLOW",
                "EXPLOSIONS_DAMAGE",
                "WILDERNESS_PISTONS",
                "WILDERNESS_DISPENSERS",
                "PLANT_GROWTH",
                "RAID_TRIGGER");
    }

    public static int valueOf(String name) {
        List<String> allflags = listFlags();

        if (allflags.indexOf(name) == 0) {
            return 1;
        } else {
            return 1 << (allflags.indexOf(name));
        }
    }

    public static String from(int flag) {
        List<String> allflags = listFlags();

        for (int i = 0; i < allflags.size(); i++) {
            int value = valueOf(allflags.get(i));

            if (value == flag) {
                return allflags.get(i);
            }
        }

        return "UNKNOWN";
    }

    public static List<List<Object>> getAsList(int flags) {
        List<String> allflags = listFlags();
        List<List<Object>> data = new ArrayList<List<Object>>();

        for (String permission : allflags) {
            int value = valueOf(permission);

            data.add(Lists.newArrayList(permission, FlagsCalculator.isFlagSet(flags, value)));
        }

        return data;
    }
}
