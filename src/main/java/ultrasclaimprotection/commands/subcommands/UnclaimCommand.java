package ultrasclaimprotection.commands.subcommands;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandsManager;

public class UnclaimCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();

            if (!LandsManager.containsPlayerUUID(player)) {
                player.sendMessage("You don't have a land.");
                return true;
            }

            if (!LandChunksManager.contains(chunk)) {
                player.sendMessage("This chunk is not claimed.");
                return true;
            }

            Player chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!chunk_owner.equals(player)) {
                player.sendMessage("You do not own this chunk.");
                return true;
            }

            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            LandChunksManager.delete(chunk);

            if (LandChunksManager.count(land_id) == 0) {
                LandsManager.delete(land_id);

                player.sendMessage("Your land was deleted because there are no chunks to maintain it.");
            };

            player.sendMessage("Successfully unclaimed the chunk.");

            return true;
        } else {
            return false;
        }
    }

}
