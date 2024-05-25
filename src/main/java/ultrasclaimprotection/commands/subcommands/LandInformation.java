package ultrasclaimprotection.commands.subcommands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.gui.LandInformationGUI;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;

public class LandInformation implements CommandExecutor {
    public static Map<UUID, Integer> cache = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1) {
                player.sendMessage(
                        ChatColorTranslator
                                .translate(Language.getString("commands.land_information.land_arg_null")));
                return true;
            }

            if (!LandsManager.containsLandName(args[1])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.land_information.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByLandName(args[1], "land_id");

            LandInformationGUI.create(player, "gui.land_information", land_id);

            cache.put(player.getUniqueId(), land_id);

            return true;
        } else {
            return false;
        }
    }
}
