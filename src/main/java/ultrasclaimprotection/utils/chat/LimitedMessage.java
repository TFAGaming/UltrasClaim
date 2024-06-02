package ultrasclaimprotection.utils.chat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import ultrasclaimprotection.UltrasClaimProtection;
import ultrasclaimprotection.utils.flags.RoleFlags;
import ultrasclaimprotection.utils.language.Language;

public class LimitedMessage {
    private static Set<UUID> cache = new HashSet<>();

    public static void send(Player player, int flag, Chunk... claimed_chunk) {
        if (!cache.contains(player.getUniqueId())) {
            player.sendMessage(ChatColorTranslator.translate(Language.getString("permissions.role_permissions", false)
                    .replace("%permission%", RoleFlags.from(flag).toLowerCase())));

            cache.add(player.getUniqueId());

            player.getServer().getScheduler().runTaskLater(UltrasClaimProtection.getPlugin(UltrasClaimProtection.class),
                    () -> cache.remove(player.getUniqueId()), 40L);
        }
    }
}
