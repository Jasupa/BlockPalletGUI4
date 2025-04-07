package org.example.bte.blockPalletGUI;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private BlockPalletManager blockPalletManager;

    @Override
    public void onEnable() {
        getLogger().info("Plugin is enabled and registering commands.");

        this.blockPalletManager = new BlockPalletManager();

        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        getCommand("blockpallet").setExecutor(new BlockPalletCommand(blockPalletManager));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InventoryClickHandler(blockPalletManager), this);
    }
}
