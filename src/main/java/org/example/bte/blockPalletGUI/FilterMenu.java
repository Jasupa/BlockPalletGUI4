package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Menu for toggling filters on and off.
 */
public class FilterMenu extends AbstractMenu {

    private final BlockPalletManager manager;

    // Base64 string for the left arrow head texture
    private static final String LEFT_ARROW =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90"
                    + "ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0"
                    + "ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYx"
                    + "YzQyMzYyMTQyYmFlMWVkZDUifX19";

    // Define the filters and their corresponding XMaterial values
    private static final LinkedHashMap<String, XMaterial> FILTERS = new LinkedHashMap<String, XMaterial>() {{
        put("slabs",            XMaterial.OAK_SLAB);
        put("stairs",           XMaterial.OAK_STAIRS);
        put("walls",            XMaterial.COBBLESTONE_WALL);
        put("logs",             XMaterial.OAK_LOG);
        put("leaves",           XMaterial.OAK_LEAVES);
        put("fences",           XMaterial.OAK_FENCE);
        put("carpet",           XMaterial.WHITE_CARPET);
        put("wool",             XMaterial.WHITE_WOOL);
        put("terracotta",       XMaterial.TERRACOTTA);
        put("concrete",         XMaterial.WHITE_CONCRETE);
        put("concrete_powder",  XMaterial.WHITE_CONCRETE_POWDER);
        put("bed",              XMaterial.RED_BED);
        put("candle",           XMaterial.CANDLE);
        put("banner",           XMaterial.WHITE_BANNER);
        put("glass_pane",       XMaterial.GLASS_PANE);
        put("signs",            XMaterial.OAK_SIGN);
        put("shulker_boxes",    XMaterial.SHULKER_BOX);
        put("gates",            XMaterial.OAK_FENCE_GATE);
        put("glass",            XMaterial.GLASS);
    }};

    // Where we store slot -> filterName, so we can handle clicks separately
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
     * Uses a glass pane as the filler item.
     *
     * @return the Mask instance to be applied before the menu is opened
     */
    @Override
    protected Mask getMask() {
        // Use the Item class to create the filler ItemStack
        ItemStack maskItem = Item.create(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial(), " ");
        return BinaryMask.builder(getMenu())
                .item(maskItem)
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .build();
    }

    /**
     * Asynchronously sets the menu items (no click handlers here).
     */
    @Override
    protected void setMenuItemsAsync() {
        Set<String> currentFilters = manager.getPlayerFilters(getMenuPlayer());
        int slot = 10;

        // Create each filter item in the menu
        for (Map.Entry<String, XMaterial> entry : FILTERS.entrySet()) {
            String filterName = entry.getKey();
            boolean active = currentFilters.contains(filterName);
            String prefix = active ? "§a✔ " : "§c✘ ";
            String displayName = prefix + capitalize(filterName);

            ItemStack filterItem = Item.create(entry.getValue().parseMaterial(), displayName);
            getMenu().getSlot(slot).setItem(filterItem);

            // Store the slot so we know which filter is placed here
            slotToFilterMap.put(slot, filterName);

            slot++;
            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }
            if (slot >= 36) break;
        }

        // Add the back button at slot 36 (we'll handle click later)
        ItemStack backItem = manager.createCustomHeadBase64(LEFT_ARROW, "§eBack");
        getMenu().getSlot(BACK_SLOT).setItem(backItem);
    }

    /**
     * Sets click event handlers for the items.
     */
    @Override
    protected void setItemClickEventsAsync() {
        // Handle filter toggling
        for (Map.Entry<Integer, String> entry : slotToFilterMap.entrySet()) {
            int slot = entry.getKey();
            String filterName = entry.getValue();

            getMenu().getSlot(slot).setClickHandler((clickPlayer, clickInfo) -> {
                Set<String> filters = manager.getPlayerFilters(clickPlayer);
                // Toggle the filter
                if (filters.contains(filterName)) {
                    filters.remove(filterName);
                } else {
                    filters.add(filterName);
                }
                manager.updatePlayerFilters(clickPlayer, filters);

                // Refresh the menu by opening a new instance
                new FilterMenu(manager, clickPlayer).open();
            });
        }

        // Handle "back" button at BACK_SLOT
        getMenu().getSlot(BACK_SLOT).setClickHandler((p, info) -> manager.openBlockMenu(p));
    }

    /**
     * Utility method to capitalize filter names.
     *
     * @param input the filter name
     * @return the capitalized string
     */
    private String capitalize(String input) {
        input = input.replace("_", " ");
        String[] parts = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase())
                    .append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * Opens the FilterMenu for the player.
     */
    public void open() {
        getMenu().open(getMenuPlayer());
    }
}
