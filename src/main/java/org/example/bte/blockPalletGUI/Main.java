package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin implements CommandExecutor, Listener {


    private static final int PAGE_SIZE = 45; // 54 total - 9 for navigation

    @Override
    public void onEnable() {
        getLogger().info("Plugin is enabled and registering commands.");

        // Initialize BlockPalletManager
        BlockPalletManager blockPalletManager = new BlockPalletManager();

        // Register the BlockPalletCommand class as the executor for the blockpallet command
        this.getCommand("blockpallet").setExecutor(new BlockPalletCommand(this, blockPalletManager));

        // Register the InventoryClickHandler for inventory click events
        getServer().getPluginManager().registerEvents(new InventoryClickHandler(blockPalletManager), this);
    }

}

