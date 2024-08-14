package veloxclaimprotection.events.teleportation;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.teleportation.DelayedTeleport;

public class PlayerMovedDelayedTP implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        String string_prefix = "";
        string_prefix += ChatColorTranslator.translate(Language.getString("commands.prefix", false));

        if (DelayedTeleport.teleport_tasks.containsKey(player.getUniqueId())) {
            if (event.getFrom().getBlockX() != event.getTo().getBlockX()
                    || event.getFrom().getBlockZ() != event.getTo().getBlockZ() || event.getFrom().getBlockY() != event.getTo().getBlockY()) {
                player.sendMessage(ChatColorTranslator.translate(
                        string_prefix + Language.getString("general.delayed_teleport.messages.player_moved")));

                BukkitTask playerTask = DelayedTeleport.teleport_tasks.get(player.getUniqueId());

                if (playerTask != null) {
                    playerTask.cancel();

                    DelayedTeleport.teleport_tasks.remove(player.getUniqueId());
                    BossBar bossBar = DelayedTeleport.teleport_bossbars.get(player.getUniqueId());

                    bossBar.removeAll();

                    DelayedTeleport.teleport_bossbars.remove(player.getUniqueId());
                }
            }
        }
    }
}