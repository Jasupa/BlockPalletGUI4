package org.example.bte.blockPalletGUI;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    // Singleton instance variable
    private static Main instance;
    private BlockPalletManager blockPalletManager;

    @Override
    public void onEnable() {
        // Initialize the instance to this object
        instance = this;

        getLogger().info("Plugin is enabled and registering commands.");

        this.blockPalletManager = new BlockPalletManager();

        registerCommands();
        registerListeners();
    }

    public static Main getInstance() {
        return instance;
    }

    private void registerCommands() {
        getCommand("blockpallet").setExecutor(new BlockPalletCommand(blockPalletManager));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InventoryClickHandler(blockPalletManager), this);
    }
}
