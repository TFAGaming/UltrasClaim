package ultrasclaimprotection.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.flags.RoleFlags;
import ultrasclaimprotection.utils.gui.ItemGUI;
import ultrasclaimprotection.utils.language.Language;

public class RolePermissionsGUI {
    public static Map<UUID, Integer> cache = new HashMap<UUID, Integer>();

    public static void create(Player player, String path, int land_id, int role_id) {
        String title = Language.getString(path + ".title", false).replace("%role_name%", (String) LandRolesManager.get(land_id, role_id, "role_name"));
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColorTranslator.translate(title));

        int role_flags = (int) LandRolesManager.get(land_id, role_id, "role_flags");
        List<List<Object>> data = RoleFlags.getAsList(role_flags);

        for (List<Object> each : data) {
            String permission = (String) each.get(0);
            boolean value = (boolean) each.get(1);

            String value_string = value ? Language.getString("general.variables.enabled", false)
                    : Language.getString("general.variables.disabled", false);

            ItemStack item = ItemGUI.getGUIItemSeperatedData(
                    Language.getString(path + ".items.displayname", false),
                    Language.getListString("items.role_permissions." + permission + ".lore"),
                    Language.getString("items.role_permissions." + permission + ".item", false), Lists.newArrayList(
                        Lists.newArrayList("%item_displayname%", Language.getString("items.role_permissions." + permission + ".displayname", false)),
                        Lists.newArrayList("%permission%", permission.toLowerCase()),
                        Lists.newArrayList("%permission_value%", value_string)
                    ));

            inventory.addItem(item);
        }

        player.openInventory(inventory);
        cache.put(player.getUniqueId(), role_id);
    }
}
