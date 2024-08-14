package veloxclaimprotection.commands.subcommands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.gui.LandInformationGUI;
import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;

public class LandInformation implements CommandExecutor {
    public static Map<UUID, Integer> cache = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();

            int land_id;

            if (LandChunksManager.contains(chunk) && args.length == 1) {
                land_id = (int) LandChunksManager.get(chunk, "land_id");
            } else {
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

                land_id = (int) LandsManager.getByLandName(args[1], "land_id");
            }

            LandInformationGUI.create(player, "gui.land_information", land_id);

            cache.put(player.getUniqueId(), land_id);

            return true;
        } else {
            return false;
        }
    }
}
