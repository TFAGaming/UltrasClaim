package veloxclaimprotection.commands.subcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.chat.Variables;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.teleportation.DelayedTeleport;

public class LandHome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
           Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.home.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            Location location = LandsManager.getLocation(land_id);

            DelayedTeleport.create(player, location, ChatColorTranslator
                    .translate(Language.getString("commands.home.teleported_success").replace("%location_details%", Variables.getLocationDetail(location))));

            return true;
        } else {
            return false;
        }
    }
}