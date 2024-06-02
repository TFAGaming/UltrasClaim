package ultrasclaimprotection.events.chunks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.flags.NaturalFlags;

public class EnvironmentProtectionListener implements Listener {
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof TNTPrimed || event.getEntity() instanceof Creeper
                || event.getEntity() instanceof Fireball || event.getEntity() instanceof EnderCrystal
                || event.getEntity().getType() == EntityType.TNT_MINECART) {
            Chunk chunk = event.getLocation().getChunk();

            if (LandChunksManager.contains(chunk)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");

                if (!LandsManager.isFlagSet(land_id, NaturalFlags.EXPLOSIONS_DAMAGE)) {
                    event.setCancelled(true);
                }
            } else {
                List<Block> allowedblocks = new ArrayList<Block>();

                for (Block block : event.blockList()) {
                    Location blocklocation = block.getLocation();
                    Chunk blockchunk = blocklocation.getChunk();

                    if (!LandChunksManager.contains(blockchunk)) {
                        allowedblocks.add(block);
                    }
                }

                event.blockList().clear();
                event.blockList().addAll(allowedblocks);
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (event.getBlock().getType().equals(Material.AIR)) {
            Chunk chunk = event.getBlock().getLocation().getChunk();

            if (LandChunksManager.contains(chunk)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");

                if (!LandsManager.isFlagSet(land_id, NaturalFlags.EXPLOSIONS_DAMAGE)) {
                    event.setCancelled(true);
                }
            } else {
                List<Block> allowedblocks = new ArrayList<Block>();

                for (Block block : event.blockList()) {
                    Location blocklocation = block.getLocation();
                    Chunk blockchunk = blocklocation.getChunk();

                    if (!LandChunksManager.contains(blockchunk)) {
                        allowedblocks.add(block);
                    }
                }

                event.blockList().clear();
                event.blockList().addAll(allowedblocks);
            }
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.getNewState().getType() == Material.FIRE) {
            Chunk chunk = event.getBlock().getLocation().getChunk();

            if (LandChunksManager.contains(chunk)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");

                if (!LandsManager.isFlagSet(land_id, NaturalFlags.FIRE_SPREAD)) {
                    event.setCancelled(true);
                }
            }
        } else {
            Chunk chunk = event.getBlock().getLocation().getChunk();

            if (LandChunksManager.contains(chunk)) {
                event.setCancelled(true);
            }
        }
    }

    public void onBlockGrow(BlockGrowEvent event) {
        Chunk chunk = event.getBlock().getLocation().getChunk();

        if (LandChunksManager.contains(chunk)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            if (!LandsManager.isFlagSet(land_id, NaturalFlags.PLANT_GROWTH)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        Chunk chunk = event.getBlock().getLocation().getChunk();

        if (LandChunksManager.contains(chunk)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            if (!LandsManager.isFlagSet(land_id, NaturalFlags.LEAVES_DECAY)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Chunk chunk = event.getBlock().getLocation().getChunk();

        if (LandChunksManager.contains(chunk)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            if (!LandsManager.isFlagSet(land_id, NaturalFlags.FIRE_SPREAD)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent event) {
        Chunk fromChunk = event.getBlock().getChunk();
        Chunk toChunk = event.getToBlock().getChunk();

        if (!fromChunk.equals(toChunk)) {
            if (LandChunksManager.contains(toChunk) && LandChunksManager.contains(fromChunk)) {
                event.setCancelled(false);
            } else if (LandChunksManager.contains(toChunk)) {
                int land_id = (int) LandChunksManager.get(toChunk, "land_id");

                if (!LandsManager.isFlagSet(land_id, NaturalFlags.LIQUID_FLOW)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        Block piston = event.getBlock();
        @SuppressWarnings({ "rawtypes", "unchecked" })
        List<Block> affectedBlocks = new ArrayList(event.getBlocks());
        BlockFace direction = event.getDirection();

        if (!affectedBlocks.isEmpty()) {
            affectedBlocks.add(piston.getRelative(direction));
        }

        if (!this.canPistonMoveBlock(affectedBlocks, direction, piston.getLocation().getChunk(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        Block piston = event.getBlock();
        @SuppressWarnings({ "rawtypes", "unchecked" })
        List<Block> affectedBlocks = new ArrayList(event.getBlocks());
        BlockFace direction = event.getDirection();

        if (event.isSticky() && !affectedBlocks.isEmpty()) {
            affectedBlocks.add(piston.getRelative(direction));
        }

        if (!this.canPistonMoveBlock(affectedBlocks, direction, piston.getLocation().getChunk(), true)) {
            event.setCancelled(true);
        }
    }

    private boolean canPistonMoveBlock(List<Block> blocks, BlockFace direction, Chunk pistonChunk,
            boolean retractOrNot) {
        @SuppressWarnings("rawtypes")
        Iterator var5;
        Block block;
        Chunk chunk;

        if (retractOrNot) {
            var5 = blocks.iterator();

            while (var5.hasNext()) {
                block = (Block) var5.next();
                chunk = block.getLocation().getChunk();

                if (!chunk.equals(pistonChunk) && LandChunksManager.contains(chunk)) {
                    OfflinePlayer pistonChunkOwner = LandChunksManager.getChunkOwner(pistonChunk);
                    OfflinePlayer chunkOwner = LandChunksManager.getChunkOwner(chunk);

                    if (pistonChunkOwner != null && chunkOwner != null
                            && pistonChunkOwner.getUniqueId().toString().equals(chunkOwner.getUniqueId().toString())) {
                        return true;
                    }

                    int land_id = (int) LandChunksManager.get(chunk, "land_id");

                    if (!LandsManager.isFlagSet(land_id, NaturalFlags.WILDERNESS_PISTONS)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            var5 = blocks.iterator();

            while (var5.hasNext()) {
                block = (Block) var5.next();
                chunk = block.getRelative(direction).getLocation().getChunk();

                if (!chunk.equals(pistonChunk) && LandChunksManager.contains(chunk)) {
                    OfflinePlayer pistonChunkOwner = LandChunksManager.getChunkOwner(pistonChunk);
                    OfflinePlayer chunkOwner = LandChunksManager.getChunkOwner(chunk);

                    if (pistonChunkOwner != null && chunkOwner != null
                            && pistonChunkOwner.getUniqueId().toString().equals(chunkOwner.getUniqueId().toString())) {
                        return true;
                    }

                    int land_id = (int) LandChunksManager.get(chunk, "land_id");

                    if (!LandsManager.isFlagSet(land_id, NaturalFlags.WILDERNESS_PISTONS)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();
        BlockData blockdata = event.getBlock().getBlockData();
        Chunk targetChunk = block.getRelative(((Directional) blockdata).getFacing()).getLocation().getChunk();

        if (!block.getLocation().getChunk().equals(targetChunk)) {
            if (LandChunksManager.contains(targetChunk)) {
                OfflinePlayer pistonChunkOwner = LandChunksManager.getChunkOwner(block.getLocation().getChunk());
                OfflinePlayer chunkOwner = LandChunksManager.getChunkOwner(targetChunk);

                if (pistonChunkOwner != null && chunkOwner != null
                        && pistonChunkOwner.getUniqueId().toString().equals(chunkOwner.getUniqueId().toString())) {
                    return;
                }

                int land_id = (int) LandChunksManager.get(targetChunk, "land_id");

                if (!LandsManager.isFlagSet(land_id, NaturalFlags.WILDERNESS_DISPENSERS)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        Block block = event.getBlock();
        Chunk chunk = block.getChunk();

        if (entity != null) {
            if (LandChunksManager.contains(chunk)) {
                if (!(entity instanceof Player) && entity instanceof Mob) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");

                    if (!LandsManager.isFlagSet(land_id, NaturalFlags.ENTITIES_GRIEF)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Chunk chunk = event.getLocation().getChunk();
        Entity entity = event.getEntity();

        if (LandChunksManager.contains(chunk)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            if (entity instanceof Monster || entity instanceof IronGolem) {
                if (!LandsManager.isFlagSet(land_id, NaturalFlags.HOSTILE_ENTITIES_SPAWN)) {
                    event.setCancelled(true);
                }
            } else if (entity instanceof Animals || entity instanceof Mob) {
                if (!LandsManager.isFlagSet(land_id, NaturalFlags.PASSIVE_ENTITIES_SPAWN)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        Chunk chunk = entity.getLocation().getChunk();

        if (LandChunksManager.contains(chunk)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            if ((damager instanceof Entity && !(damager instanceof Player)) && entity instanceof Entity) {
                if (!LandsManager.isFlagSet(land_id, NaturalFlags.ENTITIES_DAMAGE_ENTITIES)) {
                    event.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void onEntityBreakDoor(EntityBreakDoorEvent event) {
        Entity entity = event.getEntity();
        Chunk chunk = entity.getLocation().getChunk();

        if (LandChunksManager.contains(chunk)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            if (!(entity instanceof Player)) {
                if (!LandsManager.isFlagSet(land_id, NaturalFlags.ENTITIES_DAMAGE_ENTITIES)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        if (LandChunksManager.contains(chunk)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            if (!LandsManager.isFlagSet(land_id, NaturalFlags.RAID_TRIGGER)) {

                event.setCancelled(true);

                PotionEffect effect = event.getPlayer().getPotionEffect(PotionEffectType.BAD_OMEN);

                if (effect != null) {
                    event.getPlayer().removePotionEffect(PotionEffectType.BAD_OMEN);
                }
            }
        }
    }
}
