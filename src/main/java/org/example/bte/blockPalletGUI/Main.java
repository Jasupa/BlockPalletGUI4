package org.example.bte.blockPalletGUI;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private BlockPalletManager blockPalletManager;

    @Override
    public void onEnable() {
        getLogger().info("Plugin is enabled and registering commands.");

        // Initialize BlockPalletManager
        this.blockPalletManager = new BlockPalletManager();

        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        // Register the BlockPalletCommand class as the executor for the blockpallet command
        this.getCommand("blockpallet").setExecutor(new BlockPalletCommand(this, this.blockPalletManager));
    }

    private void registerListeners() {
        // Register the InventoryClickHandler for inventory click events
        getServer().getPluginManager().registerEvents(new InventoryClickHandler(this.blockPalletManager), this);
    }

}

