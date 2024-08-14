package veloxclaimprotection.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import veloxclaimprotection.managers.LandMembersManager;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;
import veloxclaimprotection.utils.chat.ChatColorTranslator;
import veloxclaimprotection.utils.language.Language;
import veloxclaimprotection.utils.player.OfflinePlayerUtils;
import veloxclaimprotection.utils.player.PlayerPermissions;

public class MembersAdd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_add.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 2) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.player_arg_null")));
                return true;
            }

            if (!OfflinePlayerUtils.playerExistsByName(args[2])) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.player_null")));
                return true;
            }

            OfflinePlayer member = OfflinePlayerUtils.getOfflinePlayerByName(args[2]);

            if (member.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.player_equal_owner")));
                return true;
            }

            if (LandMembersManager.contains(land_id, member)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.player_trusted")));
                return true;
            }

            if (args.length == 3) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.role_arg_null")));
                return true;
            }

            if (!LandRolesManager.containsByRoleName(land_id, args[3], false)) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_add.role_null")));
                return true;
            }

            int role_priority = (int) LandRolesManager.getByRoleName(land_id, args[3], "role_priority");
            
            if (role_priority == 0) {
                player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_add.role_first_priority")));
                return true;
            }

            if (PlayerPermissions.hasLandLimited(Bukkit.getPlayer(member.getUniqueId()), "trusted_lands")) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.member_limit_reached")));
                return true;
            }

            if (PlayerPermissions.hasLandLimited(player, "members")) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.member_add.limit_reached")));
                return true;
            }

            int role_id = (int) LandRolesManager.getByRoleName(land_id, args[3], "role_id");

            LandMembersManager.create(land_id, member, role_id);

            player.sendMessage(ChatColorTranslator.translate(Language.getString("commands.member_add.player_added")));

            return true;
        } else {
            return false;
        }
    }
}
