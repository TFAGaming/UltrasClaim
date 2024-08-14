package veloxclaimprotection.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.gui.ItemGUI;
import veloxclaimprotection.utils.language.Language;

public class LandInformationGUI {
    public static void create(Player player, String path, int land_id) {
        String title = Language.getString(path + ".title", false);
        int size = Language.getInt(path + ".size");
        Inventory inventory = Bukkit.createInventory(player, size, ChatColorTranslator.translate(title));

        List<Map<?, ?>> items = (List<Map<?, ?>>) Language.getMapList(path + ".items");

        for (Map<?, ?> item : items) {
            int slot = (int) item.get("slot");
            String displayname = (String) item.get("displayname");
            @SuppressWarnings("unchecked")
            List<String> lore = (List<String>) item.get("lore");
            String itemtype = (String) item.get("item");

            String land_created_at = new SimpleDateFormat("dd-MM-yyyy")
                    .format(new Date((long) LandsManager.get(land_id, "created_at")));
            
            String land_name = (String) LandsManager.get(land_id, "land_name");
            OfflinePlayer land_owner = Bukkit.getOfflinePlayer(UUID.fromString((String) LandsManager.get(land_id, "owner_uuid")));

            ItemStack itemstack = ItemGUI.getGUIItemSeperatedData(displayname, lore, itemtype, Lists.newArrayList(
                    Lists.newArrayList("%land_name%", land_name),
                    Lists.newArrayList("%land_id%", land_id),
                    Lists.newArrayList("%land_chunks%", LandChunksManager.getChunks(land_id).size()),
                    Lists.newArrayList("%land_created_at%", land_created_at),
                    Lists.newArrayList("%land_owner%", land_owner.getName())));

            inventory.setItem(slot, itemstack);
        }

        player.openInventory(inventory);
    }
}
