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

    // Base64 string voor de left arrow head texture (wordt gebruikt voor de "terug"-knop)
    private static final String LEFT_ARROW =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90"
                    + "ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0"
                    + "ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYx"
                    + "YzQyMzYyMTQyYmFlMWVkZDUifX19";

    // Map waarmee de slotindex wordt gekoppeld aan de filternaam (afgeleid van BlockPalletMenuType)
    private final Map<Integer, String> slotToFilterMap = new HashMap<>();

    private static final int BACK_SLOT = 36;

    /**
     * Construeert de FilterMenu voor de gegeven manager en speler.
     *
     * @param manager de BlockPalletManager-instantie
     * @param player  de speler voor wie het menu wordt aangemaakt
     */
    public FilterMenu(BlockPalletManager manager, Player player) {
        super(5, "Filter Menu", player);
        this.manager = manager;
    }

    /**
     * Maakt het achtergrondmasker voor het menu aan.
     * Er wordt een glas-paneel gebruikt als vulitem.
     *
     * @return het Mask dat toegepast wordt voordat het menu wordt geopend
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
     * Zet asynchroon de menu-items in (zonder de click handlers).
     * Hierbij wordt BlockPalletMenuType gebruikt als definitie van de filters.
     */
    @Override
    protected void setMenuItemsAsync() {
        // Haal de huidige filters van de speler op.
        Set<String> currentFilters = manager.getPlayerFilters(getMenuPlayer());
        slotToFilterMap.clear();
        int slot = 10;

        // Loop door alle enum-waarden; hiermee komt de definitie van de filters centraal te staan.
        for (BlockPalletMenuType type : BlockPalletMenuType.values()) {
            // Gebruik de readable name, omgezet naar lowercase en vervang spaties door underscores;
            // dit zorgt voor consistentie met de InventoryClickHandler.
            String filterKey = type.getReadableName().toLowerCase().replace(" ", "_");
            boolean active = currentFilters.contains(filterKey);
            String prefix = active ? "§a✔ " : "§c✘ ";
            String displayName = prefix + type.getReadableName();

            // Haal het icoon op via de item-supplier.
            // Als er geen geldig item is, gebruik dan een barrier als fallback.
            ItemStack[] items = type.getItemSupplier().get();
            ItemStack filterItem;
            if (items != null && items.length > 0 && items[0] != null) {
                filterItem = Item.create(items[0].getType(), displayName);
            } else {
                filterItem = Item.create(XMaterial.BARRIER.parseMaterial(), displayName);
            }

            getMenu().getSlot(slot).setItem(filterItem);
            slotToFilterMap.put(slot, filterKey);

            // Pas de slotindex aan voor de gewenste lay-out.
            slot++;
            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }
            if (slot >= 36) break;
        }

        // Voeg de "terug"-knop toe op de toegewezen slot.
        ItemStack backItem = manager.createCustomHeadBase64(LEFT_ARROW, "§eBack");
        getMenu().getSlot(BACK_SLOT).setItem(backItem);
    }

    /**
     * Zet de click event handlers voor de filter-items en de terugknop.
     */
    @Override
    protected void setItemClickEventsAsync() {
        // Verwerk het aan- en uitzetten van filters.
        for (Map.Entry<Integer, String> entry : slotToFilterMap.entrySet()) {
            int slot = entry.getKey();
            String filterName = entry.getValue();
            getMenu().getSlot(slot).setClickHandler((clickPlayer, clickInfo) -> {
                Set<String> filters = manager.getPlayerFilters(clickPlayer);
                // Toggle de filter.
                if (filters.contains(filterName)) {
                    filters.remove(filterName);
                } else {
                    filters.add(filterName);
                    // Zorg dat "color" niet geselecteerd is samen met andere filters.
                    if (!filterName.equals("color")) {
                        filters.remove("color");
                    }
                }
                manager.updatePlayerFilters(clickPlayer, filters);
                new FilterMenu(manager, clickPlayer).open();
            });
        }
        // Verwerk de "terug"-knop.
        getMenu().getSlot(BACK_SLOT).setClickHandler((player, info) -> manager.openBlockMenu(player));
    }

    /**
     * Opent de FilterMenu voor de speler.
     */
    public void open() {
        setPreviewItems();
    }
}