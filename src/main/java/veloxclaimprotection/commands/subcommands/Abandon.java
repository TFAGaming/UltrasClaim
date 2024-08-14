package veloxclaimprotection.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;

public class Abandon implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.abandon.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            LandsManager.delete(land_id);
            LandChunksManager.deleteAll(land_id);
            LandMembersManager.deleteAll(land_id);
            LandRolesManager.deleteAll(land_id);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.abandon.land_deleted")));

            return true;
        } else {
            return false;
        }
    }
}
