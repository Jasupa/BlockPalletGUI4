package org.example.bte.blockPalletGUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class BlockPalletCommand implements CommandExecutor {

    private final Main plugin;  // Reference to the main plugin class
    private final BlockPalletManager blockPalletManager;  // Reference to BlockPalletManager

    public BlockPalletCommand(Main plugin, BlockPalletManager blockPalletManager) {
        this.plugin = plugin;
        this.blockPalletManager = blockPalletManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getLogger().info("BlockPallet command received.");

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            plugin.getLogger().info("Command not sent by player.");
            return true;
        }

        Player player = (Player) sender;
        Set<String> validArgs = new HashSet<>(Set.of("color", "slabs", "stairs", "walls"));
        if (args.length == 0 || !validArgs.contains(args[0])) {
            player.sendMessage("Usage: /blockpallet color, /blockpallet slabs, /blockpallet stairs, or /blockpallet walls");
            plugin.getLogger().info("Invalid arguments.");
            return true;
        }

        plugin.getLogger().info("Argument received: " + args[0]);

        // Open the block pallet GUI for the player
        blockPalletManager.openBlockPallet(player, 0, args[0]);

        return true;  // Indicate that the command was handled
    }
}



