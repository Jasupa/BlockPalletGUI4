// src/main/java/org/example/bte/blockPalletGUI/Main.java
package org.example.bte.blockPalletGUI;

import org.bukkit.plugin.java.JavaPlugin;
// import Canvas’s listener
import org.ipvp.canvas.MenuFunctionListener;

public final class Main extends JavaPlugin {

    private static Main instance;
    private BlockPalletManager blockPalletManager;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Plugin is enabled and registering commands.");

        this.blockPalletManager = new BlockPalletManager();

        registerCommands();

        // Register Canvas’s MenuFunctionListener so all of your
        // slot click handlers actually fire in-game
        getServer().getPluginManager().registerEvents(
                new MenuFunctionListener(),
                this
        );
    }

    public static Main getInstance() {
        return instance;
    }

    private void registerCommands() {
        getCommand("blockpallet").setExecutor(
                new BlockPalletCommand(blockPalletManager)
        );
    }
}
