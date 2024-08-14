package veloxclaimprotection.commands.subcommands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.chat.LimitedMessage;
import veloxclaimprotection.utils.chat.Variables;
import veloxclaimprotection.utils.flags.RoleFlags;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.player.PlayerPermissions;
import veloxclaimprotection.utils.teleportation.DelayedTeleport;

public class Visit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1) {
                player.sendMessage(
                        ChatColorTranslator
                                .translate(Language.getString("commands.visit.land_arg_null")));
                return true;
            }

            if (!LandsManager.containsLandName(args[1])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.visit.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByLandName(args[1], "land_id");
            OfflinePlayer land_owner = Bukkit
                    .getOfflinePlayer(UUID.fromString((String) LandsManager.getByLandName(args[1], "owner_uuid")));

            if (!player.getUniqueId().toString().equals(land_owner.getUniqueId().toString())) {
                if (!PlayerPermissions.hasPermission(land_id, player, RoleFlags.TELEPORT_SPAWN)) {
                    LimitedMessage.send(player, RoleFlags.TELEPORT_SPAWN);
                    return true;
                } else if (!PlayerPermissions.hasPermission(land_id, player, RoleFlags.ENTER_LAND)) {
                    LimitedMessage.send(player, RoleFlags.ENTER_LAND);
                    return true;
                }
            }

            Location location = LandsManager.getLocation(land_id);

            DelayedTeleport.create(player, location, ChatColorTranslator
                    .translate(Language.getString("commands.visit.teleported_success").replace("%location_details%", Variables.getLocationDetail(location))));

            return true;
        } else {
            return false;
        }
    }
}
