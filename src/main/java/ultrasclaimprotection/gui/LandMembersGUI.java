package ultrasclaimprotection.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import ultrasclaimprotection.managers.LandMembersManager;
import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.gui.ItemGUI;
import ultrasclaimprotection.utils.gui.PaginationGUI;
import ultrasclaimprotection.utils.language.Language;

public class LandMembersGUI {
    public static void create(Player player, int land_id) {
        List<List<Object>> land_members = LandMembersManager.getListLandMembers(land_id);

        land_members.sort(Comparator.comparingLong((List<Object> list) -> (long) list.get(2)));

        int members_count = land_members.size();

        int total_pages = (int) Math.ceil((float) members_count / 9.0);

        if (total_pages <= 0) {
            total_pages = 1;
        }

        PaginationGUI pagegui = new PaginationGUI(player, 3,
                ChatColorTranslator.translate(Language.getString("gui.land_members.title", false)),
                total_pages);

        List<List<ItemStack>> pages = new ArrayList<>();

        for (int index = 0; index < total_pages; index++) {
            List<ItemStack> page = new ArrayList<>();

            int startIndex = index * 9;
            int endIndex = Math.min(startIndex + 9, members_count);

            for (int i = startIndex; i < endIndex; i++) {
                List<Object> member = land_members.get(i);
                String member_uuid = (String) member.get(0);
                int role_id = (int) member.get(1);
                long joined_at = (long) member.get(2);

                String role_name = (String) LandRolesManager.get(land_id, role_id, "role_name");
                String joined_at_formatted = new SimpleDateFormat("dd-MM-yyyy").format(new Date(joined_at));

                ItemStack member_item = ItemGUI.getGUIItem("gui.land_members.items",
                        Lists.newArrayList(
                                Lists.newArrayList("%member_name%",
                                        Bukkit.getOfflinePlayer(UUID.fromString(member_uuid)).getName()),
                                Lists.newArrayList("%role_name%", role_name),
                                Lists.newArrayList("%joined_at%", joined_at_formatted)));

                page.add(member_item);
            }

            pages.add(page);
        }

        for (int i = 0; i < pages.size(); i++) {
            pagegui.addPage(i, pages.get(i));
        }

        pagegui.openInventory(pagegui);
    }
}
