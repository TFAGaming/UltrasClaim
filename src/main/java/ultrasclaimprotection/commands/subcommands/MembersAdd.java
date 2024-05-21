package ultrasclaimprotection.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.managers.LandMembersManager;
import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;
import ultrasclaimprotection.utils.player.OfflinePlayerUtils;

public class MembersAdd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_add.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.player_arg_null")));
                return true;
            }

            if (!OfflinePlayerUtils.playerExistsByName(args[2])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.player_null")));
                return true;
            }

            OfflinePlayer member = OfflinePlayerUtils.getOfflinePlayerByName(args[2]);

            if (LandMembersManager.contains(land_id, member)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.player_trusted")));
                return true;
            }

            if (args.length == 3) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.role_arg_null")));
                return true;
            }

            if (!LandRolesManager.containsByRoleName(land_id, args[3], false)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_add.role_null")));
                return true;
            }

            int role_id = (int) LandRolesManager.getByRoleName(land_id, args[3], "role_id");

            LandMembersManager.create(land_id, member, role_id);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_add.player_added")));

            return true;
        } else {
            return false;
        }
    }
}
