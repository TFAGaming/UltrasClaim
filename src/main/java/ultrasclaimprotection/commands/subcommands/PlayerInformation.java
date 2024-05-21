package ultrasclaimprotection.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.gui.PlayerInformationGUI;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;
import ultrasclaimprotection.utils.player.OfflinePlayerUtils;

public class PlayerInformation implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1) {
                player.sendMessage(
                        ChatColorTranslator
                                .translate(Language.getString("commands.player_information.player_arg_null")));
                return true;
            }

            if (!OfflinePlayerUtils.playerExistsByName(args[1])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.player_information.player_not_registered")));
                return true;
            }

            OfflinePlayer player_input = OfflinePlayerUtils.getOfflinePlayerByName(args[1]);

            if (player_input == null) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.player_information.player_null")));
                return true;
            }

            PlayerInformationGUI.create(player, "gui.player_information", player_input);

            return true;
        } else {
            return false;
        }
    }
}
