package ultrasclaimprotection.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import ultrasclaimprotection.commands.subcommands.ClaimCommand;
import ultrasclaimprotection.commands.subcommands.UnclaimCommand;

public class LandsCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "claim":
                        new ClaimCommand().onCommand(sender, command, label, args);
                        break;
                    case "unclaim":
                        new UnclaimCommand().onCommand(sender, command, label, args);
                        break;
                    default:
                        break;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arraylist = new ArrayList<>();
        int currentindex = 0;

        if (args.length == 1) {
            arraylist = getSubcommands();

            currentindex = 1;
        }

        List<String> filteredlist = new ArrayList<>();

        for (String element : arraylist) {
            if (element.toLowerCase().startsWith(args[currentindex - 1].toLowerCase())) {
                filteredlist.add(element);
            }
        }

        return filteredlist;
    }

    private List<String> getSubcommands() {
        List<String> arraylist = new ArrayList<>();

        arraylist.add("claim");
        arraylist.add("unclaim");

        return arraylist;
    };
}
