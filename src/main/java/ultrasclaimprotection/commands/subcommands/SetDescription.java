package ultrasclaimprotection.commands.subcommands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.managers.LandsManager;
import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;

public class SetDescription implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!LandsManager.containsPlayer(player)) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.set_description.land_null")));
                return true;
            }

            int land_id = (int) LandsManager.getByPlayer(player, "land_id");

            if (args.length == 1) {
                player.sendMessage(
                        ChatColorTranslator
                                .translate(Language.getString("commands.set_description.land_description_arg_null")));
                return true;
            }

            System.out.println(args + " " + args.length);

            List<String> descriptionList = Arrays.asList(args).subList(1, args.length);
            String description = String.join(" ", descriptionList).replace("&#", "&");

            if (description.length() > 32) {
                player.sendMessage(
                        ChatColorTranslator.translate(Language.getString("commands.set_description.land_description_too_long")));
                return true;
            }

            LandsManager.updateDescription(land_id, description);

            player.sendMessage(
                    ChatColorTranslator.translate(Language.getString("commands.set_description.land_description_set")));

            return true;
        } else {
            return false;
        }
    }

    /*
    public static String[] sliceArray(String[] array, int startIndex, int endIndex) {
        String[] sliced = new String[endIndex - startIndex];

        for (int i = 0; i < sliced.length; i++) {
            sliced[i] = array[startIndex + i];
        }
        return sliced;
    }
    */
}
