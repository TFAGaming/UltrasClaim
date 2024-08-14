package veloxclaimprotection.utils.player;

import org.bukkit.entity.Player;

import veloxclaimprotection.VeloxClaimProtection;
import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.flags.FlagsCalculator;

public class PlayerPermissions {
    public static boolean isOperator(Player player) {
        return player.hasPermission("ultrasclaimprotection.__operator__");
    }

    public static boolean hasPermission(int land_id, Player player, int flag) {
        VeloxClaimProtection plugin = VeloxClaimProtection.getPlugin(VeloxClaimProtection.class);

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

    public static boolean hasLandLimited(Player player, String type) {
        String playergroup = VeloxClaimProtection.vaultapi.permissions.getPrimaryGroup(player);

        switch (type) {
            case "chunks":
                int groupchunkslimit = getLimitFromConfig(playergroup, "max_chunks");

                if (groupchunkslimit <= 0) {
                    groupchunkslimit = 1;
                }

                int groupchunkslimit_land_id = (int) LandsManager.getByPlayer(player, "land_id");
                int groupchunkslimit_count = LandChunksManager.count(groupchunkslimit_land_id);

                if (groupchunkslimit_count >= groupchunkslimit) {
                    return true;
                } else {
                    return false;
                }

            case "members":
                int groupmemberslimit = getLimitFromConfig(playergroup, "max_members");

                if (groupmemberslimit <= 0) {
                    groupmemberslimit = 1;
                }

                int groupmemberslimit_land_id = (int) LandsManager.getByPlayer(player, "land_id");
                int groupmemberslimit_count = LandMembersManager.count(groupmemberslimit_land_id);

                if (groupmemberslimit_count >= groupmemberslimit) {
                    return true;
                } else {
                    return false;
                }

            case "roles":
                int grouproleslimit = getLimitFromConfig(playergroup, "max_roles");

                if (grouproleslimit <= 0) {
                    grouproleslimit = 1;
                }

                int grouproleslimit_land_id = (int) LandsManager.getByPlayer(player, "land_id");
                int grouproleslimit_count = LandRolesManager.count(grouproleslimit_land_id);

                if (grouproleslimit_count >= grouproleslimit) {
                    return true;
                } else {
                    return false;
                }

            case "trusted_lands":
                int grouptrustedlandslimit = getLimitFromConfig(playergroup, "max_trusted_lands");

                if (grouptrustedlandslimit <= 0) {
                    grouptrustedlandslimit = 1;
                }

                int grouptrustedlandslimit_count = LandMembersManager.countByPlayer(player);

                if (grouptrustedlandslimit_count >= grouptrustedlandslimit) {
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    private static int getLimitFromConfig(String groupname, String limit) {
        VeloxClaimProtection plugin = VeloxClaimProtection.getPlugin(VeloxClaimProtection.class);

        return plugin.getConfig().getInt("lands.limits." + groupname + "." + limit);
    }
}
