package veloxclaimprotection.commands.subcommands;

import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.chat.Variables;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.particles.ChunkParticles;

public class Unclaim implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();

            if (!LandChunksManager.contains(chunk)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.unclaim.chunk_not_taken")));
                return true;
            }

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.unclaim.land_null")));
                return true;
            }

            OfflinePlayer chunk_owner = LandChunksManager.getChunkOwner(chunk);

            if (!chunk_owner.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.unclaim.chunk_not_owner")));
                return true;
            }

            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            LandChunksManager.delete(chunk);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.unclaim.chunk_unclaimed").replace("%chunk_details%", Variables.getChunkDetail(chunk))));

            if (LandChunksManager.count(land_id) == 0) {
                LandsManager.delete(land_id);
                LandMembersManager.deleteAll(land_id);
                LandRolesManager.deleteAll(land_id);

                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.unclaim.land_deleted")));
            } else {
                ChunkParticles.spawn(player, land_id, 1, LandChunksManager.getPlayerFlag(land_id, player));
            }

            return true;
        } else {
            return false;
        }
    }

}
