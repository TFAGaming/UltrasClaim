package ultrasclaimprotection.utils.chat;

import org.bukkit.ChatColor;

public class ChatColorTranslator {
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
