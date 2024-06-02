package ultrasclaimprotection.events.chunks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.chat.LimitedMessage;
import ultrasclaimprotection.utils.flags.RoleFlags;
import ultrasclaimprotection.utils.language.Language;
import ultrasclaimprotection.utils.player.PlayerChunkTeleportation;
import ultrasclaimprotection.utils.player.PlayerPermissions;

public class PlayerMovementChunkEntry implements Listener {
    private static final Map<UUID, Boolean> cache = new HashMap<UUID, Boolean>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = (Player) event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        if (!cache.containsKey(player.getUniqueId())) {
            cache.put(player.getUniqueId(), false);
        }

        if (LandChunksManager.contains(chunk)) {
            if (cache.get(player.getUniqueId()))
                return;

            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.ENTER_LAND)) {
                Chunk searched_chunk = PlayerChunkTeleportation.searchNearbyUnclaimedChunk(player);

                if (searched_chunk != null) {
                    PlayerChunkTeleportation.teleportPlayerToChunk(player, searched_chunk.getX(), searched_chunk.getZ(),
                            searched_chunk.getWorld(), false);

                    LimitedMessage.send(player, RoleFlags.ENTER_LAND, chunk);

                    return;
                }
            }

            String land_name = (String) LandsManager.get(land_id, "land_name");
            String land_description = (String) LandsManager.get(land_id, "land_description");

            String action_bar = ((String) Language.get("general.chunk_entry.claimed"))
                    .replace("%land_name%",
                            land_name)
                    .replace("%land_owner%", chunk_owner.getName())
                    .replace("%land_description%", land_description);

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColorTranslator.translate(action_bar)));

            cache.put(player.getUniqueId(), true);
        } else {
            if (!cache.get(player.getUniqueId()))
                return;

            String action_bar = ((String) Language.get("general.chunk_entry.unclaimed"));

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColorTranslator.translate(action_bar)));

            cache.put(player.getUniqueId(), false);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (cache.containsKey(player.getUniqueId())) {
            cache.remove(player.getUniqueId());
        }
    }
}
