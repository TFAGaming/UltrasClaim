package veloxclaimprotection.utils.chat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import veloxclaimprotection.VeloxClaimProtection;
import veloxclaimprotection.utils.flags.RoleFlags;
import veloxclaimprotection.utils.language.Language;

public class LimitedMessage {
    private static Set<UUID> cache = new HashSet<>();

    public static void send(Player player, int flag, Chunk... claimed_chunk) {
        if (!cache.contains(player.getUniqueId())) {
            player.sendMessage(ChatColorTranslator.translate(Language.getString("permissions.role_permissions", false)
                    .replace("%permission%", RoleFlags.from(flag).toLowerCase())));

            cache.add(player.getUniqueId());

            player.getServer().getScheduler().runTaskLater(VeloxClaimProtection.getPlugin(VeloxClaimProtection.class),
                    () -> cache.remove(player.getUniqueId()), 40L);
        }
    }
}
