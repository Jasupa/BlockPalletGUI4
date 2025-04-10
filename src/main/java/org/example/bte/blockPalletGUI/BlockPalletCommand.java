package org.example.bte.blockPalletGUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockPalletCommand implements CommandExecutor, TabCompleter {

    private final BlockPalletManager blockPalletManager;

    public BlockPalletCommand(BlockPalletManager blockPalletManager) {
        this.blockPalletManager = blockPalletManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            blockPalletManager.setPlayerFiltersAndOpen(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("menu")) {
            blockPalletManager.setPlayerFiltersAndOpen(player);
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("filter")) {
            new FilterMenu(blockPalletManager, player).open();
            return true;
        }

        blockPalletManager.setPlayerFiltersAndOpen(player, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> options = Arrays.asList(
                "color", "slabs", "stairs", "walls", "logs", "leaves", "fences",
                "carpet", "wool", "terracotta", "concrete", "concrete_powder", "bed",
                "candle", "banner", "glass_pane", "signs", "shulker_boxes", "gates", "glass", "menu", "filter"
        );
        if (args.length >= 1) {
            StringUtil.copyPartialMatches(args[args.length - 1], options, completions);
        }
        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }
}
