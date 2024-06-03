package ultrasclaimprotection.utils.particles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import ultrasclaimprotection.UltrasClaimProtection;
import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.utils.flags.FlagsCalculator;
import ultrasclaimprotection.utils.flags.PlayerFlags;

public class ChunkParticles {
    private static UltrasClaimProtection plugin = UltrasClaimProtection.getPlugin(UltrasClaimProtection.class);
    private static final Map<UUID, BukkitTask> cache = new HashMap<>();

    public static void spawn(Player player, int land_id, double y, int flags) {
        if (cache.containsKey(player.getUniqueId())) {
            BukkitTask taskFromMap = cache.get(player.getUniqueId());

            cancelTask(taskFromMap, player);
        }

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            spawnParticlesAroundChunk(player, land_id, player.getLocation().getY() + y, flags);
        }, 0L, 15L);

        cache.put(player.getUniqueId(), task);

        Bukkit.getScheduler().runTaskLater(plugin,
                () -> cancelTask(task, player), 60 * 20L);
    }

    private static void spawnParticlesAroundChunk(Player player, int land_id, double y, int flags) {
        List<List<Object>> chunks = LandChunksManager.getChunks(land_id);

        for (List<Object> chunk : chunks) {
            World world = player.getWorld();
            int chunkX = (Integer) chunk.get(0);
            int chunkZ = (Integer) chunk.get(1);
            String chunkWorldName = (String) chunk.get(2);
            int minX = chunkX * 16;
            int minZ = chunkZ * 16;

            if (!player.getLocation().getWorld().getName().equals(chunkWorldName))
                continue;

            DustOptions dustoptions;

            if (FlagsCalculator.isFlagSet(flags, PlayerFlags.LAND_OWNER)) {
                List<String> bordercolor = plugin.getConfig().getStringList("lands.border_colors.Owner");

                dustoptions = new DustOptions(Color.fromRGB(new Integer(bordercolor.get(0)),
                        new Integer(bordercolor.get(1)), new Integer(bordercolor.get(2))), 1.0F);
            } else if (FlagsCalculator.isFlagSet(flags, PlayerFlags.LAND_MEMBER)) {
                List<String> bordercolor = plugin.getConfig().getStringList("lands.border_colors.Trusted");

                dustoptions = new DustOptions(Color.fromRGB(new Integer(bordercolor.get(0)),
                        new Integer(bordercolor.get(1)), new Integer(bordercolor.get(2))), 1.0F);
            } else {
                List<String> bordercolor = plugin.getConfig().getStringList("lands.border_colors.Untrusted");

                dustoptions = new DustOptions(Color.fromRGB(new Integer(bordercolor.get(0)),
                        new Integer(bordercolor.get(1)), new Integer(bordercolor.get(2))), 1.0F);
            }

            Chunk north = world.getChunkAt(chunkX, chunkZ - 1);
            if (!LandChunksManager.contains(north)) {
                for (int x = minX; x < minX + 16; x++) {
                    player.spawnParticle(Particle.DUST, x, y, minZ, 5, dustoptions);
                }
            }

            Chunk south = world.getChunkAt(chunkX, chunkZ + 1);
            if (!LandChunksManager.contains(south)) {
                for (int x = minX; x < minX + 16; x++) {
                    player.spawnParticle(Particle.DUST, x, y, minZ + 16, 5, dustoptions);
                }
            }

            Chunk west = world.getChunkAt(chunkX - 1, chunkZ);
            if (!LandChunksManager.contains(west)) {
                for (int z = minZ; z < minZ + 16; z++) {
                    player.spawnParticle(Particle.DUST, minX, y, z, 5, dustoptions);
                }
            }

            Chunk east = world.getChunkAt(chunkX + 1, chunkZ);
            if (!LandChunksManager.contains(east)) {
                for (int z = minZ; z < minZ + 16; z++) {
                    player.spawnParticle(Particle.DUST, minX + 16, y, z, 5, dustoptions);
                }
            }
        }
    }

    private static void cancelTask(BukkitTask task, Player player) {
        if (task != null) {
            cache.remove(player.getUniqueId());

            task.cancel();
            task = null;
        }
    }
}
