package veloxclaimprotection.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.chat.StringUtils;
import veloxclaimprotection.utils.language.Language;

public class Rename implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.rename.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 1) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.rename.land_name_arg_null")));
                return true;
            }

            if (!StringUtils.isAlphanumericString(args[1])) {
                player.sendMessage(
                        ChatColorTranslator
                                .translate(Language.getString("commands.rename.land_name_non_alphanumeric")));
                return true;
            }

            if (args[1].length() > 16) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.rename.land_name_too_long")));
                return true;
            }

            if (LandsManager.containsLandName(args[1])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.rename.land_name_found")));
                return true;
            }

            LandsManager.rename(land_id, args[1]);

            player.sendMessage(
                    ChatColorTranslator.translate(Language.getString("commands.rename.land_renamed").replace("%land_newname%", args[1])));

            return true;
        } else {
            return false;
        }
    }
}
