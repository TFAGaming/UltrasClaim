package ultrasclaimprotection.utils.player;

import org.bukkit.entity.Player;

import ultrasclaimprotection.UltrasClaimProtection;
import ultrasclaimprotection.managers.LandMembersManager;
import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.utils.flags.FlagsCalculator;

public class PlayerPermissions {
    public static boolean isOperator(Player player) {
        return player.hasPermission("ultrasclaimprotection.__operator__");
    }

    public static boolean hasPermission(int land_id, Player player, int flag) {
        UltrasClaimProtection plugin = UltrasClaimProtection.getPlugin(UltrasClaimProtection.class);

        if (LandMembersManager.contains(land_id, player)) {
            int role_id = (int) LandMembersManager.getByPlayer(land_id, player, "role_id");
            int role_flags = (int) LandRolesManager.get(land_id, role_id, "role_flags");

            return FlagsCalculator.isFlagSet(role_flags, flag);
        } else {
            String visitor_role = plugin.getConfig().getString("lands.default_visitor_role");
            int role_flags = (int) LandRolesManager.getByRoleName(land_id, visitor_role, "role_flags");

            return FlagsCalculator.isFlagSet(role_flags, flag);
        }
    }
}
