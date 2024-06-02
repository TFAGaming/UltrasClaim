package ultrasclaimprotection.commands.subcommands;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.UltrasClaimProtection;
import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.chat.StringUtils;
import ultrasclaimprotection.utils.language.Language;
import ultrasclaimprotection.utils.particles.ChunkParticles;

public class Claim implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();

            if (LandChunksManager.contains(chunk)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.claim.chunk_taken")));
                return true;
            }

            List<String> disabled_worlds = UltrasClaimProtection.getPlugin(UltrasClaimProtection.class).getConfig()
                    .getStringList("lands.disabled_worlds");

            if (disabled_worlds != null && disabled_worlds.size() > 0
                    && disabled_worlds.contains(chunk.getWorld().getName())) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.claim.chunk_disabled_world")));
                return true;
            }

            if (!LandsManager.containsPlayer(player)) {
                if (args.length == 1) {
                    player.sendMessage(
                            ChatColorTranslator.translate(Language.getString("commands.claim.land_name_arg_null")));
                    return true;
                }

                if (!StringUtils.isAlphanumericString(args[1])) {
                    player.sendMessage(
                            ChatColorTranslator.translate(Language.getString("commands.claim.land_name_non_alphanumeric")));
                    return true;
                }

                if (LandsManager.containsLandName(args[1])) {
                    player.sendMessage(
                            ChatColorTranslator.translate(Language.getString("commands.claim.land_name_taken")));
                    return true;
                }

                UltrasClaimProtection plugin = UltrasClaimProtection.getPlugin(UltrasClaimProtection.class);

                int natural_flags = plugin.getConfig().getInt("lands.natural_flags");

                LandsManager.create(args[1], player, natural_flags);

                int land_id = (int) LandsManager.getByPlayer(player, "land_id");

                List<String> roles_list = plugin.getConfig().getStringList("lands.roles");

                for (int i = 0; i < roles_list.size(); i++) {
                    int role_permissions = plugin.getConfig().getInt("lands.role_flags." + roles_list.get(i));

                    LandRolesManager.create(land_id, roles_list.get(i), i > 1 ? 2 : i, role_permissions);
                }
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            LandChunksManager.create(land_id, chunk);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.claim.chunk_claimed")));

            ChunkParticles.spawn(player, land_id, 1, LandChunksManager.getPlayerFlagByChunk(chunk, player));

            return true;
        } else {
            return false;
        }
    }

}
