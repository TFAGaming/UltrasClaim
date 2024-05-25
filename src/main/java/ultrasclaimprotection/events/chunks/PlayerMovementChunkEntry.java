package ultrasclaimprotection.events.chunks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import ultrasclaimprotection.utils.language.Language;

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
            String land_name = (String) LandsManager.get(land_id, "land_name");
            String land_description = (String) LandsManager.get(land_id, "land_description");
            OfflinePlayer land_owner = Bukkit.getOfflinePlayer(UUID.fromString((String) LandsManager.get(land_id, "owner_uuid")));

            String action_bar = ((String) Language.get("general.chunk_entry.claimed"))
                    .replace("%land_name%",
                            land_name)
                    .replace("%land_owner%", land_owner.getName())
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
