package ultrasclaimprotection.commands.subcommands;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandsManager;

public class ClaimCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();

            if (LandChunksManager.contains(chunk)) {
                player.sendMessage("This chunk is already been claimed.");
                return true;
            }

            if (!LandsManager.containsPlayerUUID(player)) {
                if (args.length == 1) {
                    player.sendMessage("Please provide a new land name.");
                    return true;
                }

                if (LandsManager.containsLandName(args[1])) {
                    player.sendMessage("Land name already been taken.");
                    return true;
                }

                LandsManager.create(args[1], player);
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            LandChunksManager.create(land_id, chunk);

            player.sendMessage("Successfully claimed the chunk.");

            return true;
        } else {
            return false;
        }
    }

}
