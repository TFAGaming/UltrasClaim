package ultrasclaimprotection.utils.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import ultrasclaimprotection.UltrasClaimProtection;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;

public class ItemGUI {
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");

    public static ItemStack getGUIItem(String path, List<List<Object>> replacements) {
        String displayname = Language.getString(path + ".displayname", false);
        List<String> lore = Language.getListString(path + ".lore");
        String itemtype = Language.getString(path + ".item", false);

        if (replacements != null) {
            for (List<Object> replacement : replacements) {
                String replaced = displayname.replace((String) replacement.get(0), "" + replacement.get(1));

                displayname = replaced;
            }

            if (lore != null) {
                List<String> updated_lore = new ArrayList<>(lore);

                for (List<Object> replacement : replacements) {
                    String target = (String) replacement.get(0);
                    String replace = "" + replacement.get(1);

                    for (int i = 0; i < updated_lore.size(); i++) {
                        String line = updated_lore.get(i);
                        String replaced = line.replace(target, replace);

                        updated_lore.set(i, replaced);
                    }
                }

                lore = updated_lore;
            }
        }

        if (itemtype.startsWith("HEAD-")) {
            ItemStack playerhead = getCustomHeadTexture(Arrays.asList(itemtype.split("-")).get(1));

            ItemMeta meta = playerhead.getItemMeta();

            meta.setDisplayName(ChatColorTranslator.translate(displayname));

            if (lore != null) {
                ArrayList<String> lorelist = new ArrayList<String>();

                for (String each : lore) {
                    if (!each.contains("&")) {
                        each = "&f" + each;
                    }

                    lorelist.add(ChatColorTranslator.translate(each));
                }

                meta.setLore(lorelist);
            }

            playerhead.setItemMeta(meta);

            return playerhead;
        } else {
            ItemStack item = new ItemStack(Material.getMaterial(itemtype) == null ? Material.BARRIER : Material.getMaterial(itemtype));

            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColorTranslator.translate(displayname));

            if (lore != null) {
                ArrayList<String> lorelist = new ArrayList<String>();

                for (String each : lore) {
                    if (!each.contains("&")) {
                        each = "&f" + each;
                    }

                    lorelist.add(ChatColorTranslator.translate(each));
                }

                meta.setLore(lorelist);
            }

            item.setItemMeta(meta);

            return item;
        }
    }

    public static ItemStack getGUIItemSeperatedData(String displayname, List<String> lore, String itemtype, List<List<Object>> replacements) {
        if (replacements != null) {
            for (List<Object> replacement : replacements) {
                String replaced = displayname.replace((String) replacement.get(0), "" + replacement.get(1));

                displayname = replaced;
            }

            if (lore != null) {
                List<String> updated_lore = new ArrayList<>(lore);

                for (List<Object> replacement : replacements) {
                    String target = (String) replacement.get(0);
                    String replace = "" + replacement.get(1);

                    for (int i = 0; i < updated_lore.size(); i++) {
                        String line = updated_lore.get(i);
                        String replaced = line.replace(target, replace);

                        updated_lore.set(i, replaced);
                    }
                }

                lore = updated_lore;
            }
        }

        if (itemtype.startsWith("HEAD-")) {
            ItemStack playerhead = getCustomHeadTexture(Arrays.asList(itemtype.split("-")).get(1));

            ItemMeta meta = playerhead.getItemMeta();

            meta.setDisplayName(ChatColorTranslator.translate(displayname));

            if (lore != null) {
                ArrayList<String> lorelist = new ArrayList<String>();

                for (String each : lore) {
                    if (!each.contains("&")) {
                        each = "&f" + each;
                    }

                    lorelist.add(ChatColorTranslator.translate(each));
                }

                meta.setLore(lorelist);
            }

            playerhead.setItemMeta(meta);

            return playerhead;
        } else {
            ItemStack item = new ItemStack(Material.getMaterial(itemtype) == null ? Material.BARRIER : Material.getMaterial(itemtype));

            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColorTranslator.translate(displayname));

            if (lore != null) {
                ArrayList<String> lorelist = new ArrayList<String>();

                for (String each : lore) {
                    if (!each.contains("&")) {
                        each = "&f" + each;
                    }

                    lorelist.add(ChatColorTranslator.translate(each));
                }

                meta.setLore(lorelist);
            }

            item.setItemMeta(meta);

            return item;
        }
    }

    public static ItemStack getCustomHeadTexture(String texture_url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        UltrasClaimProtection plugin = UltrasClaimProtection.getPlugin(UltrasClaimProtection.class);

        meta.setOwnerProfile(
                getProfile(plugin.getConfig().getString("textures.url").replace("%profile_url%", texture_url)));
        head.setItemMeta(meta);

        return head;
    }

    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();

        URL urlobject;

        try {
            urlobject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }

        textures.setSkin(urlobject);
        profile.setTextures(textures);

        return profile;
    }
}
