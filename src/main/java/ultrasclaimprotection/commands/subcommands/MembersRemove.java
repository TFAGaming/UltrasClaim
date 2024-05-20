package ultrasclaimprotection.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.managers.LandMembersManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;

public class MembersRemove implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_remove.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_remove.player_arg_null")));
                return true;
            }

            Player member = Bukkit.getPlayer(args[2]);

            if (member == null) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_remove.player_null")));
                return true;
            }

            if (!LandMembersManager.contains(land_id, member)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_remove.player_not_trusted")));
                return true;
            }

            LandMembersManager.delete(land_id, member);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_remove.player_removed")));

            return true;
        } else {
            return false;
        }
    }
}
