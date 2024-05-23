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

import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.flags.NaturalFlags;
import ultrasclaimprotection.utils.gui.ItemGUI;
import ultrasclaimprotection.utils.language.Language;

public class NaturalFlagsGUI {
    public static Map<UUID, Integer> cache = new HashMap<UUID, Integer>();

    public static void create(Player player, String path, int land_id) {
        String title = Language.getString(path + ".title", false);
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColorTranslator.translate(title));

        int natural_flags = (int) LandsManager.get(land_id, "natural_flags");
        List<List<Object>> data = NaturalFlags.getAsList(natural_flags);

        for (List<Object> each : data) {
            String flag = (String) each.get(0);
            boolean value = (boolean) each.get(1);

            String value_string = value ? Language.getString("general.variables.enabled", false)
                    : Language.getString("general.variables.disabled", false);

            ItemStack item = ItemGUI.getGUIItemSeperatedData(
                    Language.getString(path + ".items.displayname", false),
                    Language.getListString("items.natural_flags." + flag + ".lore"),
                    Language.getString("items.natural_flags." + flag + ".item", false), Lists.newArrayList(
                            Lists.newArrayList("%item_displayname%",
                                    Language.getString("items.natural_flags." + flag + ".displayname", false)),
                            Lists.newArrayList("%flag%", flag.toLowerCase()),
                            Lists.newArrayList("%flag_value%", value_string)));

            inventory.addItem(item);
        }

        player.openInventory(inventory);
    }
}
