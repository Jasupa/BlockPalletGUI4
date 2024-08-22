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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "color":
                        blockPalletManager.openBlockPallet(player, 0, "color");
                        return true;
                    case "slabs":
                        blockPalletManager.openBlockPallet(player, 0, "slabs");
                        return true;
                    case "stairs":
                        blockPalletManager.openBlockPallet(player, 0, "stairs");
                        return true;
                    case "walls":
                        blockPalletManager.openBlockPallet(player, 0, "walls");
                        return true;
                    case "logs":
                        blockPalletManager.openBlockPallet(player, 0, "logs");
                        return true;
                    case "leaves":
                        blockPalletManager.openBlockPallet(player, 0, "leaves");
                        return true;
                    case "fences":
                        blockPalletManager.openBlockPallet(player, 0, "fences");
                        return true;
                    default:
                        player.sendMessage("Usage: /blockpallet color, /blockpallet slabs, /blockpallet stairs, /blockpallet walls, /blockpallet logs, /blockpallet leaves, or /blockpallet fences");
                        return true;
                }
            } else {
                player.sendMessage("Usage: /blockpallet color, /blockpallet slabs, /blockpallet stairs, /blockpallet walls, /blockpallet logs, /blockpallet leaves, or /blockpallet fences");
                return true;
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
        }
        return false;
    }
}


