package veloxclaimprotection.commands.subcommands;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.VeloxClaimProtection;
import veloxclaimprotection.managers.LandChunksManager;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.chat.StringUtils;
import veloxclaimprotection.utils.chat.Variables;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.particles.ChunkParticles;
import veloxclaimprotection.utils.player.PlayerPermissions;

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

            List<String> disabled_worlds = VeloxClaimProtection.getPlugin(VeloxClaimProtection.class).getConfig()
                    .getStringList("lands.disabled_worlds");

            if (disabled_worlds != null && disabled_worlds.size() > 0
                    && disabled_worlds.contains(chunk.getWorld().getName())) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.claim.chunk_disabled_world")));
                return true;
            }

            if (LandChunksManager.findNeighborChunk(player)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.claim.chunk_non_spaced")));
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
                            ChatColorTranslator
                                    .translate(Language.getString("commands.claim.land_name_non_alphanumeric")));
                    return true;
                }

                if (args[1].length() > 16) {
                    player.sendMessage(
                            ChatColorTranslator.translate(Language.getString("commands.claim.land_name_too_long")));
                    return true;
                }

                if (LandsManager.containsLandName(args[1])) {
                    player.sendMessage(
                            ChatColorTranslator.translate(Language.getString("commands.claim.land_name_taken")));
                    return true;
                }

                VeloxClaimProtection plugin = VeloxClaimProtection.getPlugin(VeloxClaimProtection.class);

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

            if (PlayerPermissions.hasLandLimited(player, "chunks")) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.claim.limit_reached")));
                return true;
            }

            LandChunksManager.create(land_id, chunk);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.claim.chunk_claimed")
                    .replace("%chunk_details%", Variables.getChunkDetail(chunk))));

            ChunkParticles.spawn(player, land_id, 1, LandChunksManager.getPlayerFlagByChunk(chunk, player));

            return true;
        } else {
            return false;
        }
    }

}
