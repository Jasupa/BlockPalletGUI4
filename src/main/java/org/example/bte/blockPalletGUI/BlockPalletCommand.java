// src/main/java/org/example/bte/blockPalletGUI/BlockPalletCommand.java
package org.example.bte.blockPalletGUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

/**
 * /bp
 * /bp menu
 * /bp filter
 * /bp filter <filter1> <filter2> …
 */
public class BlockPalletCommand implements CommandExecutor, TabCompleter {

    private final BlockPalletManager blockPalletManager;

    public BlockPalletCommand(BlockPalletManager blockPalletManager) {
        this.blockPalletManager = blockPalletManager;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;

        // /bp or /bp menu ⇒ default filters + open block menu
        if (args.length == 0
                || (args.length == 1 && args[0].equalsIgnoreCase("menu"))) {
            blockPalletManager.setPlayerFiltersAndOpen(player);
            return true;
        }

        // /bp filter ⇒ just open the filter GUI
        if (args.length == 1 && args[0].equalsIgnoreCase("filter")) {
            new FilterMenu(blockPalletManager, player).open();
            return true;
        }

        // /bp filter <f1> <f2> … ⇒ apply filters + open filter GUI
        if (args.length > 1 && args[0].equalsIgnoreCase("filter")) {
            Set<String> newFilters = new HashSet<>(
                    Arrays.asList(Arrays.copyOfRange(args, 1, args.length))
            );
            blockPalletManager.updatePlayerFilters(player, newFilters);
            new FilterMenu(blockPalletManager, player).open();
            return true;
        }

        // invalid usage ⇒ show help
        sender.sendMessage("§cUsage: §7/bp menu\n"
                + "§c   or §7/bp filter\n"
                + "§c   or §7/bp filter <filter1> <filter2> …");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command cmd,
                                      String alias,
                                      String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // only "menu" or "filter"
            List<String> top = Arrays.asList("menu", "filter");
            StringUtil.copyPartialMatches(args[0], top, completions);
        }
        else if (args.length > 1 && args[0].equalsIgnoreCase("filter")) {
            // suggest from BlockPalletMenuType.FILTER_OPTIONS
            StringUtil.copyPartialMatches(
                    args[args.length - 1],
                    BlockPalletMenuType.FILTER_OPTIONS,
                    completions
            );
        }

        Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
        return completions;
    }
}
