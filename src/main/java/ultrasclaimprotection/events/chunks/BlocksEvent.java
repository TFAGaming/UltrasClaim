package ultrasclaimprotection.events.chunks;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.utils.chat.LimitedMessage;
import ultrasclaimprotection.utils.flags.RoleFlags;
import ultrasclaimprotection.utils.player.PlayerPermissions;

public class BlocksEvent implements Listener {
    // Block place
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)
                && !event.getBlock().getType().equals(Material.FIRE)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.PLACE_BLOCKS)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.PLACE_BLOCKS, chunk);
            }
        }
    }
}
