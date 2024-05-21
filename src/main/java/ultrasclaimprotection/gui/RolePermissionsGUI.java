package ultrasclaimprotection.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.flags.FlagsCalculator;
import ultrasclaimprotection.utils.flags.RolePermissions;
import ultrasclaimprotection.utils.gui.ItemGUI;
import ultrasclaimprotection.utils.language.Language;

public class RolePermissionsGUI {
    public static void create(Player player, String path, int land_id, int role_id) {
        String title = Language.getString(path + ".title", false).replace("%role_name%", (String) LandRolesManager.get(land_id, role_id, "role_name"));
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColorTranslator.translate(title));

        System.out.println(FlagsCalculator.calculate(RolePermissions.PICKUP_ITEMS, RolePermissions.ENTER_LAND, RolePermissions.BREAK_BLOCKS,
                                RolePermissions.PLACE_BLOCKS));
        System.out.println(LandRolesManager.get(land_id, role_id, "role_flags"));

        int role_flags = (int) LandRolesManager.get(land_id, role_id, "role_flags");
        List<List<Object>> data = RolePermissions.getAsList(role_flags);

        for (List<Object> each : data) {
            String permission = (String) each.get(0);
            boolean value = (boolean) each.get(1);

            String value_string = value ? Language.getString("general.variables.enabled", false)
                    : Language.getString("general.variables.disabled", false);

            ItemStack item = ItemGUI.getGUIItemSeperatedData(
                    Language.getString(path + ".items.displayname", false),
                    Language.getList("items.role_permissions." + permission + ".lore"),
                    Language.getString("items.role_permissions." + permission + ".item", false), Lists.newArrayList(
                        Lists.newArrayList("%item_displayname%", Language.getString("items.role_permissions." + permission + ".displayname", false)),
                        Lists.newArrayList("%permission%", permission.toLowerCase()),
                        Lists.newArrayList("%permission_value%", value_string)
                    ));

            inventory.addItem(item);
        }

        player.openInventory(inventory);
    }
}
