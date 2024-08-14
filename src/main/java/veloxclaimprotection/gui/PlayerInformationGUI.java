package veloxclaimprotection.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.gui.ItemGUI;
import veloxclaimprotection.utils.language.Language;

public class PlayerInformationGUI {
    public static void create(Player player, String path, OfflinePlayer player_input) {
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

            String player_seen_formatted = new SimpleDateFormat("dd-MM-yyyy")
                    .format(new Date(player_input.getLastPlayed()));
            int player_ping = Bukkit.getPlayer(player_input.getUniqueId()) == null ? -1
                    : Bukkit.getPlayer(player_input.getUniqueId()).getPing();
            List<String> player_lands = LandMembersManager.getPlayerLandsList(player_input);
            String player_land = (String) LandsManager.getByPlayer(player_input, "land_name");

            if (player_land == null) {
                player_land = Language.getString("general.variables.null", false);
            }

            String player_status;

            if (Bukkit.getPlayer(player_input.getUniqueId()) == null) {
                player_status = Language.getString("general.variables.player_offline", false);
            } else {
                player_status = Language.getString("general.variables.player_online", false);
            }

            ItemStack itemstack = ItemGUI.getGUIItemSeperatedData(displayname, lore, itemtype, Lists.newArrayList(
                    Lists.newArrayList("%player_name%", player_input.getName()),
                    Lists.newArrayList("%player_seen_date%", player_seen_formatted),
                    Lists.newArrayList("%player_ping%", player_ping),
                    Lists.newArrayList("%player_land%", player_land),
                    Lists.newArrayList("%player_status%", player_status),
                    Lists.newArrayList("%player_lands_list%", player_lands.size() > 0 ? String.join(", ", player_lands)
                            : Language.getString("general.variables.null", false))));

            inventory.setItem(slot, itemstack);
        }

        player.openInventory(inventory);
    }
}
