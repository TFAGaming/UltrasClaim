package veloxclaimprotection.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.gui.ItemGUI;
import veloxclaimprotection.utils.gui.PaginationGUI;
import veloxclaimprotection.utils.language.Language;

public class LandClaimlistGUI {
    public static void create(Player player) {
        int land_id = (int) LandsManager.getByPlayer(player, "land_id");

        List<List<Object>> land_chunks = LandChunksManager.getChunks(land_id);

        land_chunks.sort(Comparator.comparingLong((List<Object> list) -> (long) list.get(4)));

        int chunks_count = land_chunks.size();

        int total_pages = (int) Math.ceil((float) chunks_count / 9.0);

        if (total_pages <= 0) {
            total_pages = 1;
        }

        PaginationGUI pagegui = new PaginationGUI(player, 3,
                ChatColorTranslator.translate(Language.getString("gui.land_claimlist.title", false)),
                total_pages);

        List<List<ItemStack>> pages = new ArrayList<>();

        for (int index = 0; index < total_pages; index++) {
            List<ItemStack> page = new ArrayList<>();

            int startIndex = index * 9;
            int endIndex = Math.min(startIndex + 9, chunks_count);

            for (int i = startIndex; i < endIndex; i++) {
                List<Object> chunk = land_chunks.get(i);
                int chunk_x = (int) chunk.get(0);
                int chunk_z = (int) chunk.get(1);
                String chunk_world = (String) chunk.get(2);
                int chunk_land_id = (int) chunk.get(3);
                long chunk_created = (long) chunk.get(4);

                String chunk_land_name = (String) LandsManager.get((int) chunk_land_id, "land_name");
                String created_at_formatted = new SimpleDateFormat("dd-MM-yyyy").format(new Date(chunk_created));

                ItemStack chunk_item = ItemGUI.getGUIItem("gui.land_claimlist.items",
                        Lists.newArrayList(
                                Lists.newArrayList("%chunk_x%", chunk_x),
                                Lists.newArrayList("%chunk_z%", chunk_z),
                                Lists.newArrayList("%location_x%", chunk_x * 16 + 8),
                                Lists.newArrayList("%location_z%", chunk_z * 16 + 8),
                                Lists.newArrayList("%chunk_world%", chunk_world),
                                Lists.newArrayList("%chunk_land%", chunk_land_name),
                                Lists.newArrayList("%claimed_at%", created_at_formatted)));

                page.add(chunk_item);
            }

            pages.add(page);
        }

        for (int i = 0; i < pages.size(); i++) {
            pagegui.addPage(i, pages.get(i));
        }

        pagegui.openInventory(pagegui);
    }
}
