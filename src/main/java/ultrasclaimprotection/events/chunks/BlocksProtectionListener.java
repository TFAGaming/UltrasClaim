package ultrasclaimprotection.events.chunks;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.sqlite.util.StringUtils;

import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandMembersManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.LimitedMessage;
import ultrasclaimprotection.utils.flags.RoleFlags;
import ultrasclaimprotection.utils.player.PlayerPermissions;

public class BlocksProtectionListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)
                && !event.getBlock().getType().equals(Material.FIRE)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.PLACE_BLOCKS)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.PLACE_BLOCKS, chunk);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)
                && !event.getBlock().getType().equals(Material.FIRE)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.BREAK_BLOCKS)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.BREAK_BLOCKS, chunk);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof Villager) {
            Player player = (Player) event.getPlayer();
            Villager villager = (Villager) holder;
            Chunk chunk = villager.getLocation().getChunk();

            if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.TRADE_VILLAGERS)) {
                    event.setCancelled(true);

                    LimitedMessage.send(player, RoleFlags.TRADE_VILLAGERS, chunk);
                }
            }
        } else if (event.getInventory().getHolder() instanceof org.bukkit.entity.ChestBoat
                || event.getInventory().getHolder() instanceof org.bukkit.entity.ChestedHorse
                || event.getInventory().getHolder() instanceof org.bukkit.entity.minecart.StorageMinecart
                || event.getInventory().getHolder() instanceof org.bukkit.entity.minecart.HopperMinecart) {
            Player player = (Player) event.getPlayer();
            Chunk chunk = player.getLocation().getChunk();

            if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.CONTAINERS)) {
                    event.setCancelled(true);

                    LimitedMessage.send(player, RoleFlags.CONTAINERS, chunk);
                }
            }
        }
    }

    @EventHandler
    public void onBreakCrop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedblock = event.getClickedBlock();

        if (event.getAction().equals(Action.PHYSICAL)) {
            if (clickedblock != null && clickedblock.getType() == Material.FARMLAND) {
                Chunk chunk = event.getClickedBlock().getLocation().getChunk();

                if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");
                    OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.HARVEST_CROPS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.HARVEST_CROPS, chunk);
                    }
                }
            }
        } else if (clickedblock != null && isCropBlock(clickedblock)) {
            Chunk chunk = event.getClickedBlock().getLocation().getChunk();

            if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.HARVEST_CROPS)) {
                    event.setCancelled(true);

                    LimitedMessage.send(player, RoleFlags.HARVEST_CROPS, chunk);
                }
            }
        }
    }

    private boolean isCropBlock(Block block) {
        Material type = block.getType();

        return type == Material.WHEAT || type == Material.CARROTS || type == Material.POTATOES
                || type == Material.BEETROOTS || type == Material.PITCHER_PLANT || type == Material.NETHER_WART
                || type == Material.KELP || type == Material.CACTUS || type == Material.SEA_PICKLE
                || type == Material.RED_MUSHROOM || type == Material.BROWN_MUSHROOM || type == Material.SWEET_BERRIES
                || type == Material.SWEET_BERRY_BUSH;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Chunk chunk;

        if (block == null) {
            chunk = player.getLocation().getChunk();
        } else {
            chunk = block.getLocation().getChunk();
        }

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
            if (event.getItem() != null) {
                if (event.getItem().getType().name().contains("BOAT")
                        || event.getItem().getType().name().contains("ARMOR_STAND")
                        || event.getItem().getType().name().contains("MINECART")
                        || event.getItem().getType().name().contains("PAINTING")
                        || event.getItem().getType().name().contains("BONE_MEAL")) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");
                    OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.PLACE_BLOCKS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.PLACE_BLOCKS, chunk);

                        return;
                    }
                }
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Material material = event.getClickedBlock().getType();

                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (material.name().contains("CHEST") || material.equals(Material.FURNACE)
                        || material.equals(Material.SMOKER) || material.equals(Material.BLAST_FURNACE)
                        || material.equals(Material.BREWING_STAND) || material.equals(Material.BARREL)
                        || material.equals(Material.SHULKER_BOX) || material.equals(Material.BEACON)
                        || material.equals(Material.DROPPER) || material.equals(Material.DISPENSER)
                        || material.equals(Material.CHISELED_BOOKSHELF) || material.name().contains("ANVIL")) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.CONTAINERS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.CONTAINERS, chunk);
                    }
                } else if (material.name().contains("TRAPDOOR")) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.TRAP_DOORS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.TRAP_DOORS, chunk);
                    }
                } else if (material.name().contains("DOOR")) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.DOORS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.DOORS, chunk);
                    }
                } else if (material.name().contains("SIGN")) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.SIGNS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.SIGNS, chunk);
                    }
                } else if (material.name().contains("BUTTON")) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.BUTTONS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.BUTTONS, chunk);
                    }
                } else if (material.name().contains("FENCE_GATE")) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.FENCE_GATES)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.FENCE_GATES, chunk);
                    }
                } else if (material.name().contains("POT")
                        || material.name().contains("CANDLE")
                        || material.equals(Material.CAKE)
                        || material.equals(Material.DAYLIGHT_DETECTOR)) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.GENERAL_INTERACTION)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.GENERAL_INTERACTION, chunk);
                    }
                } else if (material.equals(Material.LEVER)) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.LEVERS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.LEVERS, chunk);
                    }
                } else if (material.equals(Material.REPEATER) || material.equals(Material.COMPARATOR)
                        || material.equals(Material.COMMAND_BLOCK) || material.equals(Material.COMMAND_BLOCK_MINECART)
                        || material.equals(Material.REDSTONE) || material.equals(Material.REDSTONE_WIRE)
                        || material.equals(Material.NOTE_BLOCK) || material.equals(Material.JUKEBOX)
                        || material.equals(Material.COMPOSTER) || material.equals(Material.BELL)) {
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.REDSTONE)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.REDSTONE, chunk);
                    }
                }
            } else {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (event.getAction() == Action.PHYSICAL) {
                    if (block != null && block.getType().name().contains("PRESSURE_PLATE")) {
                        if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                                && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.PRESSURE_PLATES)) {
                            event.setCancelled(true);

                            LimitedMessage.send(player, RoleFlags.PRESSURE_PLATES, chunk);
                        }
                    }

                    // if (block != null) { if (block.getType() == Material.TRIPWIRE) { } }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            Player player = event.getPlayer();
            Chunk chunk = event.getRightClicked().getLocation().getChunk();

            if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.ARMOR_STANDS)) {
                    event.setCancelled(true);

                    LimitedMessage.send(player, RoleFlags.ARMOR_STANDS, chunk);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPunchFrame(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.ITEM_FRAME || event.getEntityType() == EntityType.GLOW_ITEM_FRAME) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                Chunk chunk = event.getEntity().getLocation().getChunk();

                if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");
                    OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);
                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.CONTAINERS)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.CONTAINERS, chunk);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event) {
        Player player = event.getPlayer();
        Block block = event.getLectern().getLocation().getBlock();
        Chunk chunk = block.getLocation().getChunk();

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.CONTAINERS)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.CONTAINERS, chunk);
            }
        }
    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Chunk chunk = event.getBlock().getChunk();

            ItemStack boots = player.getEquipment().getBoots();

            if (boots != null && boots.getEnchantments().containsKey(Enchantment.FROST_WALKER)) {
                if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                    int land_id = (int) LandChunksManager.get(chunk, "land_id");
                    OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                    if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                            && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.FROST_WALKER)) {
                        event.setCancelled(true);

                        LimitedMessage.send(player, RoleFlags.FROST_WALKER, chunk);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME
                || event.getRightClicked().getType() == EntityType.GLOW_ITEM_FRAME) {
            Player player = event.getPlayer();
            Chunk chunk = event.getRightClicked().getLocation().getChunk();

            if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
                int land_id = (int) LandChunksManager.get(chunk, "land_id");
                OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

                if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                        && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.GENERAL_INTERACTION)) {
                    event.setCancelled(true);

                    LimitedMessage.send(player, RoleFlags.GENERAL_INTERACTION, chunk);
                }
            }
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getLocation().getChunk();

        if (player == null) {
            event.setCancelled(true);

            return;
        }

        if (player != null && LandChunksManager.contains(chunk) && !PlayerPermissions.isOperator(player)) {
            int land_id = (int) LandChunksManager.get(chunk, "land_id");
            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!player.getUniqueId().toString().equals(chunk_owner.getUniqueId().toString())
                    && !PlayerPermissions.hasPermission(land_id, player, RoleFlags.IGNITE)) {
                event.setCancelled(true);

                LimitedMessage.send(player, RoleFlags.IGNITE, chunk);
            }
        }
    }
}
