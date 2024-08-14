package veloxclaimprotection.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.chat.Variables;
import veloxclaimprotection.utils.language.Language;

public class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.set_spawn.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            LandsManager.updateLocation(land_id, player);

            player.sendMessage(
                    ChatColorTranslator.translate(Language.getString("commands.set_spawn.land_spawnpoint_updated")
                            .replace("%location_details%", Variables.getLocationDetail(player.getLocation()))));

            return true;
        } else {
            return false;
        }
    }
}
