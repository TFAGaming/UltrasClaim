package veloxclaimprotection.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.gui.RolePermissionsGUI;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;

public class RolePermissions implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.role_permissions.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_permissions.role_arg_null")));
                return true;
            }

            if (!LandRolesManager.containsByRoleName(land_id, args[2], true)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_permissions.role_null")));
                return true;
            }

            RolePermissionsGUI.create(player, "gui.role_permissions", land_id, (int) LandRolesManager.getByRoleName(land_id, args[2], "role_id"));

            return true;
        } else {
            return false;
        }
    }
}
