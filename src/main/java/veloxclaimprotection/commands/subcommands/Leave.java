package veloxclaimprotection.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;

public class Leave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.leave.land_arg_null")));
                return true;
            }

            if (!LandsManager.containsLandName(args[1])) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.leave.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByLandName(args[1], "land_id");

            if (!LandMembersManager.contains(land_id, player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.leave.player_not_trusted")));
                return true;
            }

            LandMembersManager.delete(land_id, player);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.leave.player_removed")));

            return true;
        } else {
            return false;
        }
    }
}
