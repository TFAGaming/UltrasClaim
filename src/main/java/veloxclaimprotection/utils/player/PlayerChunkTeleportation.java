package veloxclaimprotection.utils.player;

import javax.annotation.Nullable;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.utils.teleportation.DelayedTeleport;

public class PlayerChunkTeleportation {
    public static void teleportPlayerToChunk(Player player, int chunk_x, int chunk_z, World world, boolean delayed) {
        Location location = new Location(world, chunk_x * 16 + 8, 64, chunk_z * 16 + 8);

        location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
        location.setPitch(player.getLocation().getPitch());
        location.setYaw(player.getLocation().getYaw());

        if (delayed) {
            DelayedTeleport.create(player, location, null);
        } else {
            player.teleport(location);
        }
    }

    @Nullable
    public static Chunk searchNearbyUnclaimedChunk(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        World world = player.getWorld();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        String chunkWorldName = chunk.getWorld().getName();

        if (!player.getLocation().getWorld().getName().equals(chunkWorldName))
            return null;

        Chunk north = world.getChunkAt(chunkX, chunkZ - 1);
        Chunk south = world.getChunkAt(chunkX, chunkZ + 1);
        Chunk west = world.getChunkAt(chunkX - 1, chunkZ);
        Chunk east = world.getChunkAt(chunkX + 1, chunkZ);

        if (!LandChunksManager.contains(north)) {
            return north;
        } else if (!LandChunksManager.contains(south)) {
            return south;
        } else if (!LandChunksManager.contains(west)) {
            return west;
        } else if (!LandChunksManager.contains(east)) {
            return east;
        } else {
            return null;
        }
    }
}
