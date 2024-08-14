package veloxclaimprotection.commands.subcommands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;

public class RoleDelete implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.role_delete.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_delete.role_arg_null")));
                return true;
            }

            if (!LandRolesManager.containsByRoleName(land_id, args[2], false)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_delete.role_null")));
                return true;
            }

            int role_priority = (int) LandRolesManager.getByRoleName(land_id, args[2], "role_priority");

            if (role_priority == 0 || role_priority == 1) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.role_delete.role_undeletable")));
                return true;
            }

            if (landMemberHasRole(land_id, (int) LandRolesManager.getByRoleName(land_id, args[2], "role_id"))) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.role_delete.trusted_player_has_role")));
                return true;
            }

            LandRolesManager.delete(land_id, (int) LandRolesManager.getByRoleName(land_id, args[2], "role_id"));

            player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_delete.role_deleted")));

            return true;
        } else {
            return false;
        }
    }

    private boolean landMemberHasRole(int land_id, int role_id) {
        boolean value = false;

        List<List<Object>> data = LandMembersManager.getListLandMembers(land_id);

        for (List<Object> each : data) {
            if ((int) each.get(1) == role_id) {
                value = true;
                break;
            }
        }

        return value;
    }
}
