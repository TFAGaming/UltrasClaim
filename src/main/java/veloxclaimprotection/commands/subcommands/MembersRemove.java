package veloxclaimprotection.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.player.OfflinePlayerUtils;

public class MembersRemove implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_remove.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_remove.player_arg_null")));
                return true;
            }

            if (!OfflinePlayerUtils.playerExistsByName(args[2])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_remove.player_null")));
                return true;
            }

            OfflinePlayer member = OfflinePlayerUtils.getOfflinePlayerByName(args[2]);

            if (!LandMembersManager.contains(land_id, member)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_remove.player_not_trusted")));
                return true;
            }

            LandMembersManager.delete(land_id, member);

            player.sendMessage(
                    ChatColorTranslator.translate(Language.getString("commands.member_remove.player_removed")));

            return true;
        } else {
            return false;
        }
    }
}
