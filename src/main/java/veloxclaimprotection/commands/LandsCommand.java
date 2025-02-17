package veloxclaimprotection.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import veloxclaimprotection.commands.subcommands.Abandon;
import veloxclaimprotection.commands.subcommands.Claim;
import veloxclaimprotection.commands.subcommands.ClaimList;
import veloxclaimprotection.commands.subcommands.Help;
import veloxclaimprotection.commands.subcommands.LandHome;
import veloxclaimprotection.commands.subcommands.LandInformation;
import veloxclaimprotection.commands.subcommands.LandView;
import veloxclaimprotection.commands.subcommands.Leave;
import veloxclaimprotection.commands.subcommands.MembersAdd;
import veloxclaimprotection.commands.subcommands.MembersList;
import veloxclaimprotection.commands.subcommands.MembersPosition;
import veloxclaimprotection.commands.subcommands.MembersRemove;
import veloxclaimprotection.commands.subcommands.NaturalFlags;
import veloxclaimprotection.commands.subcommands.PlayerInformation;
import veloxclaimprotection.commands.subcommands.Rename;
import veloxclaimprotection.commands.subcommands.RoleCreate;
import veloxclaimprotection.commands.subcommands.RoleDelete;
import veloxclaimprotection.commands.subcommands.RolePermissions;
import veloxclaimprotection.commands.subcommands.RoleRename;
import veloxclaimprotection.commands.subcommands.SetDescription;
import veloxclaimprotection.commands.subcommands.SetSpawn;
import veloxclaimprotection.commands.subcommands.Unclaim;
import veloxclaimprotection.commands.subcommands.Visit;
import veloxclaimprotection.managers.LandRolesManager;
import veloxclaimprotection.managers.LandsManager;

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
                        new LandView().onCommand(sender, command, label, args);
                        break;
                    case "claimlist":
                        new ClaimList().onCommand(sender, command, label, args);
                        break;
                    case "player":
                        new PlayerInformation().onCommand(sender, command, label, args);
                        break;
                    case "nature":
                        new NaturalFlags().onCommand(sender, command, label, args);
                        break;
                    case "info":
                        new LandInformation().onCommand(sender, command, label, args);
                        break;
                    case "rename":
                        new Rename().onCommand(sender, command, label, args);
                        break;
                    case "visit":
                        new Visit().onCommand(sender, command, label, args);
                        break;
                    case "setspawn":
                        new SetSpawn().onCommand(sender, command, label, args);
                        break;
                    case "setdescription":
                        new SetDescription().onCommand(sender, command, label, args);
                        break;
                    case "abandon":
                        new Abandon().onCommand(sender, command, label, args);
                        break;
                    case "help":
                        new Help().onCommand(sender, command, label, args);
                        break;
                    case "leave":
                        new Leave().onCommand(sender, command, label, args);
                        break;
                    case "home":
                        new LandHome().onCommand(sender, command, label, args);
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
                    case "position":
                        new MembersPosition().onCommand(sender, command, label, args);
                        break;
                    default:
                        break;
                }
            } else if (args.length > 1 && args[0].equalsIgnoreCase("roles")) {
                switch (args[1]) {
                    case "create":
                        new RoleCreate().onCommand(sender, command, label, args);
                        break;
                    case "delete":
                        new RoleDelete().onCommand(sender, command, label, args);
                        break;
                    case "rename":
                        new RoleRename().onCommand(sender, command, label, args);
                        break;
                    case "flags":
                        new RolePermissions().onCommand(sender, command, label, args);
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
                    arraylist.add("position");
                    break;
                case "roles":
                    arraylist.add("create");
                    arraylist.add("delete");
                    arraylist.add("rename");
                    arraylist.add("flags");
                    break;
                case "player":
                    arraylist = LandsManager.getPlayerNames();
                    break;
                case "info":
                    arraylist = LandsManager.getListLandNames();
                    break;
                case "visit":
                    arraylist = LandsManager.getListLandNames();
                    break;
                case "leave":
                    arraylist = LandsManager.getListLandNames();
                    break;
            }

            currentindex = 2;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("members")) {
            switch (args[1]) {
                case "add":
                    arraylist = LandsManager.getPlayerNames();
                    break;
                case "remove":
                    arraylist = LandsManager.getPlayerNames();
                    break;
                case "position":
                    arraylist = LandsManager.getPlayerNames();
                    break;
                default:
                    break;
            }

            currentindex = 3;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("roles")) {
            switch (args[1]) {
                case "create":
                    break;
                case "delete":
                    if (LandsManager.containsPlayer((Player) sender)) {
                        int land_id = (int) LandsManager.getByPlayer((Player) sender, "land_id");

                        arraylist = LandRolesManager.getRoles(land_id);
                    }

                    break;
                case "rename":
                    if (LandsManager.containsPlayer((Player) sender)) {
                        int land_id = (int) LandsManager.getByPlayer((Player) sender, "land_id");

                        arraylist = LandRolesManager.getRoles(land_id);
                    }

                    break;
                case "flags":
                    if (LandsManager.containsPlayer((Player) sender)) {
                        int land_id = (int) LandsManager.getByPlayer((Player) sender, "land_id");

                        arraylist = LandRolesManager.getRoles(land_id);
                    }

                    break;
                default:
                    break;
            }

            currentindex = 3;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("members")
                && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("position"))) {
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

    private List<String> getSubcommands() {
        List<String> arraylist = new ArrayList<>();

        arraylist.add("claim");
        arraylist.add("unclaim");
        arraylist.add("members");
        arraylist.add("view");
        arraylist.add("claimlist");
        arraylist.add("player");
        arraylist.add("roles");
        arraylist.add("nature");
        arraylist.add("info");
        arraylist.add("rename");
        arraylist.add("visit");
        arraylist.add("setspawn");
        arraylist.add("setdescription");
        arraylist.add("abandon");
        arraylist.add("help");
        arraylist.add("leave");
        arraylist.add("home");

        return arraylist;
    };
}
