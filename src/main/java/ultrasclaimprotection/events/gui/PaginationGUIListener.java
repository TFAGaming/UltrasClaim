package ultrasclaimprotection.events.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import ultrasclaimprotection.events.gui.pagination.ClaimlistGUIListener;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.gui.PaginationGUI;
import ultrasclaimprotection.utils.language.Language;

public class PaginationGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (PaginationGUI.inventory_cache.containsKey(player.getUniqueId().toString())) {
            event.setCancelled(true);

            Inventory inventory = PaginationGUI.inventory_cache.get(player.getUniqueId().toString());
            int inventory_size = inventory.getSize() / 9;

            int first_index_last_line = 9 * (inventory_size - 1);
            int last_index_last_line = (9 * inventory_size) - 1;

            PaginationGUI pagegui = PaginationGUI.pagegui_cache.get(player.getUniqueId().toString());

            if (event.getSlot() == first_index_last_line) {
                pagegui.previousPage();
            } else if (event.getSlot() == last_index_last_line) {
                pagegui.nextPage();
            } else {
                String inventory_name = event.getView().getTitle();

                if (inventory_name.startsWith(ChatColorTranslator.translate(Language.getString("gui.land_claimlist.title", false)))) {
                    ClaimlistGUIListener.listen(event, player, pagegui);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (PaginationGUI.inventory_cache.containsKey(player.getUniqueId().toString())) {
            PaginationGUI.inventory_cache.remove(player.getUniqueId().toString());
            PaginationGUI.pagegui_cache.remove(player.getUniqueId().toString());
        }
    }
}