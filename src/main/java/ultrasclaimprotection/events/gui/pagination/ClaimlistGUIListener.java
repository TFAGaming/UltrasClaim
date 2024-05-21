package ultrasclaimprotection.events.gui.pagination;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.gui.PaginationGUI;
import ultrasclaimprotection.utils.language.Language;
import ultrasclaimprotection.utils.teleportation.DelayedTeleport;

public class ClaimlistGUIListener {
    public static void listen(InventoryClickEvent event, Player player, PaginationGUI pagegui) {
        int clicked_chunk = event.getSlot();

        if (clicked_chunk > (9 * 3) - 1) {
            return;
        }

        int land_id = (int) LandsManager.getByPlayer(player, "land_id");

        List<List<Object>> chunks = LandChunksManager.getChunks(land_id);

        chunks.sort(Comparator.comparingLong((List<Object> list) -> (long) list.get(4)));

        int pageIndex = pagegui.getPage();
        int slotIndex = event.getSlot();

        if (pageIndex >= 0 && slotIndex >= 0) {
            int itemsPerPage = 9;
            int chunkIndex = pageIndex * itemsPerPage + slotIndex;

            if (chunkIndex < chunks.size()) {
                List<Object> chunk = chunks.get(chunkIndex);
                int chunk_x = (Integer) chunk.get(0);
                int chunk_z = (Integer) chunk.get(1);
                String chunk_world = (String) chunk.get(2);

                player.closeInventory();

                Location location = new Location(Bukkit.getWorld(chunk_world), chunk_x * 16 + 8, 64, chunk_z * 16 + 8);

                location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
                location.setPitch(player.getLocation().getPitch());
                location.setYaw(player.getLocation().getYaw());

                DelayedTeleport.create(player, location,
                        Language.getString("commands.claimlist.teleport_success", true)
                                .replace("%chunk_x%", "" + chunk_x).replace("%chunk_z%", "" + chunk_z)
                                .replace("%chunk_world%", chunk_world));
            }
        }
    }
}
