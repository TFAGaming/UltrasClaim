package ultrasclaimprotection.events.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;

public class NormalGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String inventory_title = event.getView().getTitle();

        if (inventory_title.startsWith(ChatColorTranslator.translate(Language.getString("gui.player_information.title", false)))) {
            event.setCancelled(true);
        }
    }
}