package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A paginated block list menu using AbstractPaginatedMenu as a base.
 */
public class BlockListMenu extends AbstractPaginatedMenu {
    private final BlockPalletManager manager;

    /**
     * Constructs the block list menu and initializes controls.
     * @param manager block pallet manager
     * @param player  the player to display the menu to
     */
    public BlockListMenu(BlockPalletManager manager, Player player) {
        // 6 rows total, PAGE_SIZE items spreads over (PAGE_SIZE/9) rows
        super(6, BlockPalletManager.PAGE_SIZE / 9, "Block Pallet Menu", player, true);
        this.manager = manager;
    }

    /**
     * Collects all available block items based on current filters.
     */
    @Override
    protected List<ItemStack> getSource() {
        List<String> filters = manager.getFilters(getMenuPlayer());
        if (filters.isEmpty()) {
            filters = Collections.singletonList("color");
        }
        ItemStack[] items = manager.getItemsForFilters(filters);
        return Arrays.asList(items);
    }

    /**
     * Places placeholder items (and static controls) synchronously.
     */
    @Override
    protected void setPaginatedPreviewItems(List<?> pageItems) {
        setupBorderAndControls();

        @SuppressWarnings("unchecked")
        List<ItemStack> items = (List<ItemStack>) pageItems;
        for (int i = 0; i < items.size(); i++) {
            int slot = 9 + i;
            if (slot >= 45) break;
            getMenu().getSlot(slot).setItem(items.get(i));
        }

        int totalPages = (int) Math.ceil((double) getSource().size() / BlockPalletManager.PAGE_SIZE);
        String pageText = getPage() + "/" + Math.max(totalPages, 1);
        getMenu().getSlot(49)
                .setItem(manager.createCustomHeadBase64(BlockPalletManager.HEAD_BETWEEN_ARROWS, pageText));
    }

    /**
     * Places actual menu items asynchronously (optional, mirrors preview).
     */
    @Override
    protected void setPaginatedMenuItemsAsync(List<?> pageItems) {
        // because our preview already loads real items there is nothing heavy to do here
        setPaginatedPreviewItems(pageItems);
    }

    /**
     * Sets click events for each item slot asynchronously.
     */
    @Override
    protected void setPaginatedItemClickEventsAsync(List<?> pageItems) {
        @SuppressWarnings("unchecked")
        List<ItemStack> items = (List<ItemStack>) pageItems;
        for (int i = 0; i < items.size(); i++) {
            int slot = 9 + i;
            if (slot >= 45) break;
            final ItemStack item = items.get(i);
            getMenu().getSlot(slot).setClickHandler((p, info) -> p.getInventory().addItem(item.clone()));
        }
    }

    /**
     * Supplies the border mask for automatic application.
     */
    @Override
    protected Mask getMask() {
        Menu menu = getMenu();
        return BinaryMask.builder(menu)
                .item(manager.createMenuItem(XMaterial.GRAY_STAINED_GLASS_PANE, " "))
                .pattern("111111111")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("111111111")
                .build();
    }

    /**
     * Bridges AbstractMenu's setItemClickEventsAsync requirement.
     */


    /**
     * Sets up static border, filter button, and navigation arrows.
     */
    private void setupBorderAndControls() {
        Menu menu = getMenu();
        Mask background = getMask();
        background.apply(menu);

        // filter button at slot 4
        ItemStack filter = manager.createMenuItem(XMaterial.HOPPER, "Filter Menu");
        menu.getSlot(4).setItem(filter);
        menu.getSlot(4).setClickHandler((p, info) -> new FilterMenu(manager, p).open());

        // previous and next arrows
        menu.getSlot(48)
                .setItem(manager.createCustomHeadBase64(BlockPalletManager.LEFT_ARROW, "Previous Page"));
        menu.getSlot(50)
                .setItem(manager.createCustomHeadBase64(BlockPalletManager.RIGHT_ARROW, "Next Page"));

        // placeholder for page indicator (actual text set in preview)
        menu.getSlot(49)
                .setItem(manager.createCustomHeadBase64(BlockPalletManager.HEAD_BETWEEN_ARROWS, ""));

        // attach click handlers for paging
        setSwitchPageItemClickEvents(49);
    }

    /**
     * Opens the menu for the player.
     */
    public static void open(BlockPalletManager manager, Player player) {
        BlockListMenu menu = new BlockListMenu(manager, player);
        menu.getMenu().open(player);
    }
    /**
     * Required by AbstractMenu: set click handlers after async loading.
     */
    @Override
    protected void setItemClickEventsAsync() {
        // delegate to paginated click wiring using sources for this page
        List<?> sources = getSource();
        int from = (getPage() - 1) * BlockPalletManager.PAGE_SIZE;
        int to = Math.min(from + BlockPalletManager.PAGE_SIZE, sources.size());
        setPaginatedItemClickEventsAsync(sources.subList(from, to));
    }
    /**
     * Required by AbstractMenu: place items asynchronously once loaded.
     */
    @Override
    protected void setMenuItemsAsync() {
        List<?> sources = getSource();
        int from = (getPage() - 1) * BlockPalletManager.PAGE_SIZE;
        int to = Math.min(from + BlockPalletManager.PAGE_SIZE, sources.size());
        setPaginatedMenuItemsAsync(sources.subList(from, to));
    }
}
