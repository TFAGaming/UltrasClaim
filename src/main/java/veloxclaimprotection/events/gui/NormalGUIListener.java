package veloxclaimprotection.events.gui;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import veloxclaimprotection.commands.subcommands.LandInformation;
import veloxclaimprotection.gui.LandMembersGUI;
import veloxclaimprotection.gui.RolePermissionsGUI;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.flags.FlagsCalculator;
import veloxclaimprotection.utils.flags.NaturalFlags;
import veloxclaimprotection.utils.flags.RoleFlags;
import veloxclaimprotection.utils.gui.ItemGUI;
import veloxclaimprotection.utils.language.Language;

public class NormalGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventory_title = event.getView().getTitle();

        if (inventory_title
                .startsWith(ChatColorTranslator.translate(Language.getString("gui.player_information.title", false)))) {
            event.setCancelled(true);
        } else if (inventory_title.startsWith(ChatColorTranslator
                .translate(Language.getString("gui.role_permissions.title", false).replace("%role_name%", "")))) {
            event.setCancelled(true);

            if (!event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                int slot = event.getSlot();

                int land_id = (int) LandsManager.getByPlayer(player, "land_id");
                int role_id = RolePermissionsGUI.cache.get(player.getUniqueId());

                int role_flags = (int) LandRolesManager.get(land_id, role_id, "role_flags");
                List<List<Object>> data = RoleFlags.getAsList(role_flags);

                if (slot > data.size() - 1) {
                    return;
                }

                String permission = (String) data.get(slot).get(0);
                boolean current_value = (boolean) data.get(slot).get(1);

                boolean new_value = !current_value;
                int new_flags = 0;

                if (new_value) {
                    new_flags = FlagsCalculator.calculate(role_flags, RoleFlags.valueOf(permission));
                } else {
                    new_flags = FlagsCalculator.removeFlag(role_flags, RoleFlags.valueOf(permission));
                }

                LandRolesManager.updateFlags(land_id, role_id, new_flags);

                replaceItemRolePermissionsGUI(event.getInventory(), land_id, role_id, permission, new_value, slot);
            }
        } else if (inventory_title.startsWith(ChatColorTranslator
                .translate(Language.getString("gui.natural_flags.title", false)))) {
            event.setCancelled(true);

            if (!event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                int slot = event.getSlot();

                int land_id = (int) LandsManager.getByPlayer(player, "land_id");

                int natural_flags = (int) LandsManager.get(land_id, "natural_flags");
                List<List<Object>> data = NaturalFlags.getAsList(natural_flags);

                if (slot > data.size() - 1) {
                    return;
                }

                String flag = (String) data.get(slot).get(0);
                boolean current_value = (boolean) data.get(slot).get(1);

                boolean new_value = !current_value;
                int new_flags = 0;

                if (new_value) {
                    new_flags = FlagsCalculator.calculate(natural_flags, NaturalFlags.valueOf(flag));
                } else {
                    new_flags = FlagsCalculator.removeFlag(natural_flags, NaturalFlags.valueOf(flag));
                }

                LandsManager.updateNaturalFlags(land_id, new_flags);

                replaceItemNaturalFlagsGUI(event.getInventory(), land_id, flag, new_value, slot);
            }
        } else if (inventory_title.startsWith(ChatColorTranslator
                .translate(Language.getString("gui.land_information.title", false)))) {
            event.setCancelled(true);

            if (!event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                int slot = event.getSlot();

                List<Map<?, ?>> items = (List<Map<?, ?>>) Language.getMapList("gui.land_information.items");

                for (Map<?, ?> item : items) {
                    int item_slot = (int) item.get("slot");
                    Object item_action_object = item.get("action");

                    if (item_slot == slot && item_action_object != null) {
                        if ((int) item_action_object == 1 && LandInformation.cache.containsKey(player.getUniqueId())) {
                            int land_id = LandInformation.cache.get(player.getUniqueId());

                            event.getView().close();

                            LandMembersGUI.create(player, land_id);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (RolePermissionsGUI.cache.containsKey(player.getUniqueId())) {
            RolePermissionsGUI.cache.remove(player.getUniqueId());
        }

        if (LandInformation.cache.containsKey(player.getUniqueId())) {
            LandInformation.cache.remove(player.getUniqueId());
        }
    }

    private static void replaceItemRolePermissionsGUI(Inventory inventory, int land_id, int role_id, String permission,
            boolean new_value, int slot) {
        String value_string = new_value ? Language.getString("general.variables.enabled", false)
                : Language.getString("general.variables.disabled", false);

        ItemStack item = ItemGUI.getGUIItemSeperatedData(
                Language.getString("gui.role_permissions.items.displayname", false),
                Language.getListString("items.role_permissions." + permission + ".lore"),
                Language.getString("items.role_permissions." + permission + ".item", false), Lists.newArrayList(
                        Lists.newArrayList("%item_displayname%",
                                Language.getString("items.role_permissions." + permission + ".displayname", false)),
                        Lists.newArrayList("%permission%", permission.toLowerCase()),
                        Lists.newArrayList("%permission_value%", value_string)));

        inventory.setItem(slot, item);
    }

    private static void replaceItemNaturalFlagsGUI(Inventory inventory, int land_id, String flag,
            boolean new_value, int slot) {
        String value_string = new_value ? Language.getString("general.variables.enabled", false)
                : Language.getString("general.variables.disabled", false);

        ItemStack item = ItemGUI.getGUIItemSeperatedData(
                Language.getString("gui.natural_flags.items.displayname", false),
                Language.getListString("items.natural_flags." + flag + ".lore"),
                Language.getString("items.natural_flags." + flag + ".item", false), Lists.newArrayList(
                        Lists.newArrayList("%item_displayname%",
                                Language.getString("items.natural_flags." + flag + ".displayname", false)),
                        Lists.newArrayList("%flag%", flag.toLowerCase()),
                        Lists.newArrayList("%flag_value%", value_string)));

        inventory.setItem(slot, item);
    }
}