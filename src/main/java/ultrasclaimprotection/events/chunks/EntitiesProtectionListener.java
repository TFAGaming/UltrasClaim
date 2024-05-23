package ultrasclaimprotection.events.chunks;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.LimitedMessage;
import ultrasclaimprotection.utils.flags.NaturalFlags;
import ultrasclaimprotection.utils.flags.RoleFlags;
import ultrasclaimprotection.utils.player.PlayerPermissions;

public class EntitiesProtectionListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        Chunk chunk = entity.getLocation().getChunk();

        if (LandChunksManager.contains(chunk)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (damager instanceof Player && entity instanceof ArmorStand) {
                if (!PlayerPermissions.isOperator((Player) damager)
                        && !((Player) damager).getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, (Player) damager, RoleFlags.BREAK_BLOCKS)) {
                    event.setCancelled(true);

                    LimitedMessage.send((Player) damager, RoleFlags.BREAK_BLOCKS, chunk);
                }
            } else if (entity instanceof Player && damager instanceof Player) {
                if (!PlayerPermissions.hasPermission(land_id, (Player) damager, RoleFlags.PVP)) {
                    event.setCancelled(true);

                    LimitedMessage.send((Player) damager, RoleFlags.PVP, chunk);
                }
            } else if (damager instanceof Player && (entity instanceof Monster || entity instanceof IronGolem)) {
                if (!((Player) damager).getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.isOperator((Player) damager)
                        && !PlayerPermissions.hasPermission(land_id, (Player) damager,
                                RoleFlags.DAMAGE_HOSTILE_ENTITIES)) {
                    event.setCancelled(true);

                    LimitedMessage.send((Player) damager, RoleFlags.DAMAGE_HOSTILE_ENTITIES, chunk);
                }
            } else if (damager instanceof Player && (entity instanceof Animals || entity instanceof Mob)) {
                if (!((Player) damager).getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.isOperator((Player) damager)
                        && !PlayerPermissions.hasPermission(land_id, (Player) damager,
                                RoleFlags.DAMAGE_PASSIVE_ENTITIES)) {
                    event.setCancelled(true);

                    LimitedMessage.send((Player) damager, RoleFlags.DAMAGE_PASSIVE_ENTITIES, chunk);
                }
            } else if (damager instanceof Creeper || damager instanceof TNTPrimed || damager instanceof Fireball
                    || damager instanceof EnderCrystal || damager.getType() == EntityType.TNT_MINECART) {
                if (!LandsManager.isFlagSet(land_id, NaturalFlags.ENTITIES_DAMAGE_ENTITIES)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getEntity().getLocation().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.INTERACT_ENTITIES)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.INTERACT_ENTITIES, chunk);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = (Player) event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        if (event.getItem() != null) {
            if (event.getItem().getType().equals(Material.ENDER_PEARL)) {
                if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");
                    OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.THROW_ENDER_PEARLS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.THROW_ENDER_PEARLS, chunk);
                    }
                }
            } else if (event.getItem().getType().equals(Material.SPLASH_POTION)
                    || event.getItem().getType().equals(Material.LINGERING_POTION)) {
                if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");
                    OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.THROW_POTIONS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.THROW_POTIONS, chunk);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        ProjectileSource source = event.getEntity().getShooter();
        Chunk chunk = event.getEntity().getLocation().getChunk();
        Entity entityhit = event.getHitEntity();

        if (source instanceof Player && LandChunksManager.contains(chunk)) {
            Player player = (Player) source;

            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (entityhit instanceof Player) {
                if (!PlayerPermissions.hasPermission(land_id, player, RoleFlags.PVP)) {
                    event.setCancelled(true);
                    event.getEntity().remove();

                    LimitedMessage.send(player, RoleFlags.PVP, chunk);
                }
            } else if (entityhit instanceof Monster || entityhit instanceof IronGolem) {
                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.DAMAGE_HOSTILE_ENTITIES)) {
                    event.setCancelled(true);
                    event.getEntity().remove();

                    LimitedMessage.send(player, RoleFlags.DAMAGE_HOSTILE_ENTITIES, chunk);
                }
            } else if (entityhit instanceof Animals || entityhit instanceof Mob) {
                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.DAMAGE_PASSIVE_ENTITIES)) {
                    event.setCancelled(true);
                    event.getEntity().remove();

                    LimitedMessage.send(player, RoleFlags.DAMAGE_PASSIVE_ENTITIES, chunk);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = (Player) event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.PICKUP_ITEMS)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.PICKUP_ITEMS, chunk);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerDropItemEvent event) {
        Player player = (Player) event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.PICKUP_ITEMS)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.PICKUP_ITEMS, chunk);
            }
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Vehicle vehicle = event.getVehicle();
        Chunk chunk = vehicle.getLocation().getChunk();
        Entity entity = event.getEntered();

        if (vehicle != null) {
            if (entity instanceof Player && LandChunksManager.contains(chunk)
                    && !PlayerPermissions.isOperator((Player) entity)) {
                Player player = (Player) entity;

                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.VEHICLES)) {
                    event.setCancelled(true);

                    LimitedMessage.send(player, RoleFlags.VEHICLES, chunk);
                }
            }
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        Vehicle vehicle = event.getVehicle();
        Chunk chunk = vehicle.getLocation().getChunk();
        Entity entity = event.getAttacker();

        if (vehicle != null) {
            if (entity instanceof Player && LandChunksManager.contains(chunk)
                    && !PlayerPermissions.isOperator((Player) entity)) {
                Player player = (Player) entity;

                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.BREAK_BLOCKS)) {
                    event.setCancelled(true);

                    LimitedMessage.send(player, RoleFlags.BREAK_BLOCKS, chunk);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        Player player = (Player) event.getPlayer();
        Entity entity = event.getEntity();
        Chunk chunk = entity.getLocation().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.INTERACT_ENTITIES)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.INTERACT_ENTITIES, chunk);
            }
        }
    }

    @EventHandler
    public void onLeashEvent(PlayerLeashEntityEvent event) {
        Player player = event.getPlayer();
        Block block = event.getEntity().getLocation().getBlock();
        Chunk chunk = block.getLocation().getChunk();

        if (block.getType().name().contains("FENCE")) {
            if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.INTERACT_ENTITIES)) {
                    event.setCancelled(true);

                    LimitedMessage.send(player, RoleFlags.INTERACT_ENTITIES, chunk);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        Player player = (Player) event.getPlayer();
        Entity entity = event.getEntity();
        Chunk chunk = entity.getLocation().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.INTERACT_ENTITIES)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.INTERACT_ENTITIES, chunk);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        Chunk chunk = entity.getLocation().getChunk();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.NAME_TAG)) {
            if (entity instanceof Entity) {
                if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");
                    OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.INTERACT_ENTITIES)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.INTERACT_ENTITIES, chunk);
                    }
                }
            }
        } else if (item.getType().name().contains("DYE")) {
            if (entity instanceof Sheep || entity instanceof Wolf || entity instanceof Cat) {
                if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");
                    OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.INTERACT_ENTITIES)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.INTERACT_ENTITIES, chunk);
                    }
                }
            }
        }
    }
}
