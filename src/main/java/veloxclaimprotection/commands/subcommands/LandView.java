package veloxclaimprotection.commands.subcommands;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.particles.ChunkParticles;

public class LandView implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();

            if (!LandChunksManager.contains(chunk)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.view.chunk_not_taken")));
                return true;
            }

            int land_id = (int) LandChunksManager.get(chunk, "land_id");

            ChunkParticles.spawn(player, land_id, 1, LandChunksManager.getPlayerFlagByChunk(chunk, player));

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.view.view_success")));

            return true;
        } else {
            return false;
        }
    }
}