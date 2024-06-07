package ultrasclaimprotection.commands.subcommands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ultrasclaimprotection.utils.chat.ChatColorTranslator;
import ultrasclaimprotection.utils.language.Language;

public class Help implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            List<String> messages = Language.getListString("commands.help.message");
            String finalString = "";

            for (String message : messages) {
                finalString += (message + "\n");
            }

            player.sendMessage(ChatColorTranslator.translate(finalString));

            return true;
        } else {
            return false;
        }
    }
}
