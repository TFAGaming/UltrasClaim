package ultrasclaimprotection.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import ultrasclaimprotection.commands.subcommands.Claim;
import ultrasclaimprotection.commands.subcommands.ClaimList;
import ultrasclaimprotection.commands.subcommands.MembersAdd;
import ultrasclaimprotection.commands.subcommands.MembersList;
import ultrasclaimprotection.commands.subcommands.MembersRemove;
import ultrasclaimprotection.commands.subcommands.Unclaim;
import ultrasclaimprotection.commands.subcommands.View;
import ultrasclaimprotection.managers.LandRolesManager;
import ultrasclaimprotection.managers.LandsManager;

public class LandsCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "claim":
                        new Claim().onCommand(sender, command, label, args);
                        break;
                    case "unclaim":
                        new Unclaim().onCommand(sender, command, label, args);
                        break;
                    case "members":
                        if (args.length != 1) {
                            break;
                        }

                        new MembersList().onCommand(sender, command, label, args);
                        break;
                    case "view":
                        new View().onCommand(sender, command, label, args);
                        break;
                    case "claimlist":
                        new ClaimList().onCommand(sender, command, label, args);
                        break;
                    default:
                        break;
                }
            }

            if (args.length > 1 && args[0].equalsIgnoreCase("members")) {
                switch (args[1]) {
                    case "add":
                        new MembersAdd().onCommand(sender, command, label, args);
                        break;
                    case "remove":
                        new MembersRemove().onCommand(sender, command, label, args);
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
        } else if (args.length == 2) {
            switch (args[0]) {
                case "members":
                    arraylist.add("add");
                    arraylist.add("remove");

                    break;
            }

            currentindex = 2;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("members")) {
            switch (args[1]) {
                case "add":
                    arraylist = getOnlinePlayers();
                    break;
                case "remove":
                    arraylist = getOnlinePlayers();
                    break;
                default:
                    break;
            }

            currentindex = 3;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("members") && args[1].equalsIgnoreCase("add")) {
            if (LandsManager.containsPlayer((Player) sender)) {
                int land_id = (int) LandsManager.getByPlayer((Player) sender, "land_id");

                arraylist = LandRolesManager.getRoles(land_id);

                currentindex = 4;
            }
        }

        List<String> filteredlist = new ArrayList<>();

        for (String element : arraylist) {
            if (element.toLowerCase().startsWith(args[currentindex - 1].toLowerCase())) {
                filteredlist.add(element);
            }
        }

        return filteredlist;
    }

    private static List<String> getOnlinePlayers() {
        List<String> playernames = new ArrayList<>();
        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];

        Bukkit.getServer().getOnlinePlayers().toArray(players);

        for (int i = 0; i < players.length; i++) {
            playernames.add(players[i].getName());
        }

        return playernames;
    }

    private List<String> getSubcommands() {
        List<String> arraylist = new ArrayList<>();

        arraylist.add("claim");
        arraylist.add("unclaim");
        arraylist.add("members");
        arraylist.add("view");
        arraylist.add("claimlist");

        return arraylist;
    };
}
