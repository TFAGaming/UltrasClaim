package veloxclaimprotection.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.player.OfflinePlayerUtils;

public class MembersPosition implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_position.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_position.player_arg_null")));
                return true;
            }

            if (!OfflinePlayerUtils.playerExistsByName(args[2])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_position.player_null")));
                return true;
            }

            OfflinePlayer member = OfflinePlayerUtils.getOfflinePlayerByName(args[2]);

            if (!LandMembersManager.contains(land_id, member)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_position.player_not_trusted")));
                return true;
            }

            if (args.length == 3) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_position.role_arg_null")));
                return true;
            }

            if (!LandRolesManager.containsByRoleName(land_id, args[3], false)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_position.role_null")));
                return true;
            }

            int role_priority = (int) LandRolesManager.getByRoleName(land_id, args[3], "role_priority");
            
            if (role_priority == 0) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_position.role_first_priority")));
                return true;
            }

            int role_id = (int) LandRolesManager.getByRoleName(land_id, args[3], "role_id");

            LandMembersManager.updatePlayerPosition(land_id, member, role_id);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_position.player_updated_position")));

            return true;
        } else {
            return false;
        }
    }
}
