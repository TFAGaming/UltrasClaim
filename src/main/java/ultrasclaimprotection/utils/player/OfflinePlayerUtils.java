package ultrasclaimprotection.utils.player;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import ultrasclaimprotection.managers.LandsManager;

public class OfflinePlayerUtils {
    public static boolean playerExistsByName(String name) {
        List<String> playernames = LandsManager.getPlayerNames();

        return playernames.contains(name);
    }

    public static OfflinePlayer getOfflinePlayerByName(String name) {
        List<String> playernames = LandsManager.getPlayerNames();

        return Bukkit.getOfflinePlayer(UUID.fromString(LandsManager.getPlayerUUIDs().get(playernames.indexOf(name))));
    }
}
