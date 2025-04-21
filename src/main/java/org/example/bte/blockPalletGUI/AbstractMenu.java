package org.example.bte.blockPalletGUI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.type.ChestMenu;

public abstract class AbstractMenu {

    private final Menu menu;
    private final Player menuPlayer;

    /**
     * Constructor that reloads the menu asynchronously by default.
     *
     * @param rows       The number of rows in the menu.
     * @param title      The title of the menu (supports legacy & color codes).
     * @param menuPlayer The player for whom the menu is created.
     */
    public AbstractMenu(int rows, String title, Player menuPlayer) {
        this(rows, title, menuPlayer, true);
    }

    /**
     * Constructor that optionally reloads the menu.
     *
     * @param rows       The number of rows in the menu.
     * @param title      The title of the menu (supports legacy & color codes).
     * @param menuPlayer The player for whom the menu is created.
     * @param reload     Whether to asynchronously reload the menu items.
     */
    public AbstractMenu(int rows, String title, Player menuPlayer, boolean reload) {
        this.menuPlayer = menuPlayer;

        // Create a component by deserializing legacy color codes.
        Component titleComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(title);
        // Convert the component back into a string as Canvas expects a String for the title.
        String finalTitle = LegacyComponentSerializer.legacyAmpersand().serialize(titleComponent);

        // Build the ChestMenu with the string title.
        this.menu = ChestMenu.builder(rows)
                .title(finalTitle)
                .redraw(true)
                .build();

        if (reload) reloadMenuAsync();
    }

    /**
     * Places items asynchronously in the menu after it is opened.
     */
    protected abstract void setMenuItemsAsync();

    /**
     * Sets click events for the items placed in the menu asynchronously after it is opened.
     */
    protected abstract void setItemClickEventsAsync();

    /**
     * Provides a pre-defined mask to place items into the menu before it is opened.
     *
     * @return Pre-defined Mask or {@code null} if no mask is used.
     */
    protected abstract Mask getMask();

    /**
     * Applies pre-defined items to the menu and opens it for the player.
     */
    protected void setPreviewItems() {
        Mask mask = getMask();
        if (mask != null) {
            mask.apply(getMenu());
        }
        getMenu().open(getMenuPlayer());
    }

    /**
     * Reloads all menu items and click events asynchronously.
     * First sets preview items synchronously then loads additional items asynchronously.
     */
    protected void reloadMenuAsync() {
        setPreviewItems();

        // Schedule asynchronous task using Main.getInstance()
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try {
                setMenuItemsAsync();
                setItemClickEventsAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Update the menu for the player once complete.
            menu.update(getMenuPlayer());
        });
    }

    /**
     * Gets the underlying menu.
     *
     * @return The menu.
     */
    protected Menu getMenu() {
        return menu;
    }

    /**
     * Returns the player associated with this menu.
     *
     * @return The player.
     */
    protected Player getMenuPlayer() {
        return menuPlayer;
    }
}