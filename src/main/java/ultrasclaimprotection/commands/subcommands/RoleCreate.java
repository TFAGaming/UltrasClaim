package ultrasclaimprotection.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.UltrasClaimProtection;
import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.chat.StringUtils;
import ultrasclaimprotection.utils.flags.FlagsCalculator;
import ultrasclaimprotection.utils.language.Language;
import ultrasclaimprotection.utils.player.PlayerPermissions;

public class RoleCreate implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.role_create.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_create.role_arg_null")));
                return true;
            }

            if (!StringUtils.isAlphanumericString(args[2])) {
                player.sendMessage(
                        ChatColorTranslator
                                .translate(Language.getString("commands.role_create.role_name_non_alphanumeric")));
                return true;
            }

            if (args[2].length() > 16) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_create.role_name_too_long")));
                return true;
            }

            if (LandRolesManager.containsByRoleName(land_id, args[2], true)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_create.role_found")));
                return true;
            }

            if (PlayerPermissions.hasLandLimited(player, "roles")) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.role_create.limit_reached")));
                return true;
            }

            LandRolesManager.create(land_id, args[2], 2,
                    FlagsCalculator.calculate(UltrasClaimProtection.getPlugin(UltrasClaimProtection.class).getConfig().getInt("lands.default_role_flags")));

            player.sendMessage(
                    ChatColorTranslator.translate(Language.getString("commands.role_create.role_created").replace("%role_name%", args[2])));

            return true;
        } else {
            return false;
        }
    }
}
