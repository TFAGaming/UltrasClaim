package ultrasclaimprotection.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.gui.NaturalFlagsGUI;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;

public class NaturalFlags implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.natural_flags.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            NaturalFlagsGUI.create(player, "gui.natural_flags", land_id);

            return true;
        } else {
            return false;
        }
    }
}