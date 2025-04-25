package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FilterMenu extends AbstractMenu {

    private final BlockPalletManager manager;

    // Base64 string for the left arrow head texture (used for the "back" button)
    private static final String LEFT_ARROW =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90"
                    + "ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0"
                    + "ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYx"
                    + "YzQyMzYyMTQyYmFlMWVkZDUifX19";

    // Maps slot index to filter name (derived from BlockPalletMenuType)
    private final Map<Integer, String> slotToFilterMap = new HashMap<>();

    private static final int BACK_SLOT = 36;

    /**
     * Constructs the FilterMenu for the given manager and player.
     *
     * @param manager the BlockPalletManager instance
     * @param player  the player for whom the menu is created
     */
    public FilterMenu(BlockPalletManager manager, Player player) {
        super(5, "Filter Menu", player);
        this.manager = manager;
    }

    /**
     * Creates the background mask for the menu.
     * A glass pane is used as the filler item.
     *
     * @return the Mask applied before the menu is opened
     */
    @Override
    protected Mask getMask() {
        ItemStack maskItem = Item.create(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial(), " ");
        return BinaryMask.builder(getMenu())
                .item(maskItem)
                .pattern("111111111")
                .pattern("100000001")
                .pattern("100000001")
                .pattern("100011111")
                .pattern("011111111")
                .build();
    }

    /**
     * Asynchronously sets the menu items (without click handlers).
     * BlockPalletMenuType is used as the definition of filters.
     */
    @Override
    protected void setMenuItemsAsync() {
        // Retrieve the current filters of the player.
        Set<String> currentFilters = manager.getPlayerFilters(getMenuPlayer());
        slotToFilterMap.clear();
        int slot = 10;

        // Loop through all enum values; centralizing filter definitions.
        for (BlockPalletMenuType type : BlockPalletMenuType.values()) {
            // Use the readable name, converted to lowercase and spaces replaced with underscores;
            // this ensures consistency with the InventoryClickHandler.
            String filterKey = type.getReadableName().toLowerCase().replace(" ", "_");
            boolean active = currentFilters.contains(filterKey);
            String prefix = active ? "§a✔ " : "§c✘ ";
            String displayName = prefix + type.getReadableName();

            // Retrieve the icon via the item supplier.
            // If there's no valid item, use a barrier as a fallback.
            ItemStack[] items = type.getItemSupplier().get();
            ItemStack filterItem;
            if (items != null && items.length > 0 && items[0] != null) {
                filterItem = Item.create(items[0].getType(), displayName);
            } else {
                filterItem = Item.create(XMaterial.BARRIER.parseMaterial(), displayName);
            }

            getMenu().getSlot(slot).setItem(filterItem);
            slotToFilterMap.put(slot, filterKey);

            // Adjust the slot index for the desired layout.
            slot++;
            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }
            if (slot >= 36) break;
        }

        // Add the "back" button to the designated slot.
        ItemStack backItem = manager.createCustomHeadBase64(LEFT_ARROW, "§eBack");
        getMenu().getSlot(BACK_SLOT).setItem(backItem);
    }

    /**
     * Sets the click event handlers for the filter items and the back button.
     */
    @Override
    protected void setItemClickEventsAsync() {
        // Handle toggling filters on and off.
        for (Map.Entry<Integer, String> entry : slotToFilterMap.entrySet()) {
            int slot = entry.getKey();
            String filterName = entry.getValue();
            getMenu().getSlot(slot).setClickHandler((clickPlayer, clickInfo) -> {
                Set<String> filters = manager.getPlayerFilters(clickPlayer);
                // Toggle the filter.
                if (filters.contains(filterName)) {
                    filters.remove(filterName);
                } else {
                    filters.add(filterName);
                    // Ensure that "color" is not selected alongside other filters.
                    if (!filterName.equals("color")) {
                        filters.remove("color");
                    }
                }
                manager.updatePlayerFilters(clickPlayer, filters);
                new FilterMenu(manager, clickPlayer).open();
            });
        }
        // Handle the "back" button.
        getMenu().getSlot(BACK_SLOT).setClickHandler((player, info) -> manager.openBlockMenu(player));
    }

    /**
     * Opens the FilterMenu for the player.
     */
    public void open() {
        setPreviewItems();
    }
}
