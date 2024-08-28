package org.example.bte.blockPalletGUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlockPalletCommand implements CommandExecutor {

    private final BlockPalletManager blockPalletManager;

    public BlockPalletCommand(BlockPalletManager blockPalletManager) {
        this.blockPalletManager = blockPalletManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
        }
            Player player = (Player) sender;
            if (args.length > 0) {
                blockPalletManager.openBlockPallet(player, 0, args[0].toLowerCase());
            } else {
                String[] options = {
                        "color", "slabs", "stairs", "walls", "logs", "leaves", "fences", "glass",
                        "carpet", "wool", "terracotta", "concrete", "concrete_powder", "bed",
                        "candle", "banner", "glass_pane"
                };

                String optionsList = String.join(" | ", options);

                String usageMessage = String.format("Usage: /blockpallet <%s>", optionsList);
                player.sendMessage(usageMessage);
                return true;
            }
            return false;
    }
}



