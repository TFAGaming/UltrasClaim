package ultrasclaimprotection.commands.subcommands;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.managers.LandChunksManager;
import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.flags.FlagsCalculator;
import ultrasclaimprotection.utils.flags.RoleFlags;
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

            if (!LandsManager.containsPlayer(player)) {
                if (args.length == 1) {
                    player.sendMessage(
                            ChatColorTranslator.translate(Language.getString("commands.claim.land_name_arg_null")));
                    return true;
                }

                if (LandsManager.containsLandName(args[1])) {
                    player.sendMessage(
                            ChatColorTranslator.translate(Language.getString("commands.claim.land_name_taken")));
                    return true;
                }

                LandsManager.create(args[1], player);

                int land_id = (int) LandsManager.getByPlayer(player, "land_id");

                LandRolesManager.create(land_id, "Visitor", 0,
                        FlagsCalculator.calculate(RoleFlags.PICKUP_ITEMS, RoleFlags.ENTER_LAND));
                LandRolesManager.create(land_id, "Member", 1,
                        FlagsCalculator.calculate(RoleFlags.PICKUP_ITEMS, RoleFlags.ENTER_LAND, RoleFlags.BREAK_BLOCKS,
                                RoleFlags.PLACE_BLOCKS));
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
