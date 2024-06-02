package ultrasclaimprotection.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.chat.StringUtils;
import ultrasclaimprotection.utils.language.Language;

public class RoleRename implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.role_rename.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_rename.role_arg_null")));
                return true;
            }

            if (!LandRolesManager.containsByRoleName(land_id, args[2], false)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_rename.role_null")));
                return true;
            }

            if (args.length == 3) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_rename.role_newname_arg_null")));
                return true;
            }

            if (!StringUtils.isAlphanumericString(args[3])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_rename.role_newname_non_alphanumeric")));
                return true;
            }

            if (LandRolesManager.containsByRoleName(land_id, args[3], true)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_rename.role_newname_found")));
                return true;
            }

            LandRolesManager.rename(land_id, (int) LandRolesManager.getByRoleName(land_id, args[2], "role_id"), args[3]);

            player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_rename.role_renamed")));

            return true;
        } else {
            return false;
        }
    }
}
