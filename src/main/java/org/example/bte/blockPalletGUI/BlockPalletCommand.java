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
            return true;  // Added return here after checking sender type
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
            BlockPalletManager.openBlockPalletMenu(player);
            return true;
        }

        if (args.length > 0) {
            blockPalletManager.openBlockPallet(player, 0, args[0].toLowerCase());
        } else {
            String[] options = {
                    "color", "slabs", "stairs", "walls", "logs", "leaves", "fences", "glass",
                    "carpet", "wool", "terracotta", "concrete", "concrete_powder", "bed",
                    "candle", "banner", "glass_pane", "signs", "shulker_boxes", "gates"
            };

            String optionsList = String.join(" | ", options);

            String usageMessage = String.format("Usage: /blockpallet <%s>", optionsList);
            player.sendMessage(usageMessage);
            return true;
        }

        return false;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> options = Arrays.asList("color", "slabs", "stairs", "walls", "logs", "leaves", "fences",
                "carpet", "wool", "terracotta", "concrete", "concrete_powder", "bed",
                "candle", "banner", "glass_pane", "signs", "shulker_boxes", "gates", "glass", "menu");

        // Handle completion for the first argument
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], options, completions);
        }

        // Sort results
        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }
}


