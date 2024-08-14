package veloxclaimprotection.utils.teleportation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import veloxclaimprotection.VeloxClaimProtection;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;

import java.util.HashMap;
import java.util.UUID;

public class DelayedTeleport extends JavaPlugin {
    public static HashMap<UUID, BukkitTask> teleport_tasks = new HashMap<>();
    public static HashMap<UUID, BossBar> teleport_bossbars = new HashMap<>();

    public static void create(Player player, Location location, String message) {
        if (teleport_tasks.containsKey(player.getUniqueId())) {
            String string_prefix = "";
            string_prefix += ChatColorTranslator.translate(Language.getString("commands.prefix", false));

            player.sendMessage(ChatColorTranslator.translate(
                    string_prefix + Language.getString("general.delayed_teleport.messages.delay_already_exist")));

            return;
        }

        BossBar bossBar = Bukkit.createBossBar(
                ChatColorTranslator.translate(Language.getString("general.delayed_teleport.bossbar.title", false)),
                BarColor.valueOf(Language.getString("general.delayed_teleport.bossbar.color", false)),
                BarStyle.valueOf(Language.getString("general.delayed_teleport.bossbar.style", false)));
        bossBar.setProgress(1.0);
        bossBar.addPlayer(player);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(VeloxClaimProtection.getPlugin(VeloxClaimProtection.class), () -> {
            double progress = bossBar.getProgress();
            progress -= 1.0 / 60;

            if (progress <= 0) {
                player.teleport(location);
                bossBar.removeAll();

                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 500.0f, 1.0f);

                BukkitTask playerTask = teleport_tasks.get(player.getUniqueId());

                if (playerTask != null) {
                    playerTask.cancel();

                    teleport_tasks.remove(player.getUniqueId());
                    teleport_bossbars.remove(player.getUniqueId());

                    if (message != null) {
                        player.sendMessage(ChatColorTranslator.translate(message));
                    }
                }
            } else {
                bossBar.setProgress(progress);
            }
        }, 0L, 1L);

        teleport_tasks.put(player.getUniqueId(), task);
        teleport_bossbars.put(player.getUniqueId(), bossBar);
    }
}