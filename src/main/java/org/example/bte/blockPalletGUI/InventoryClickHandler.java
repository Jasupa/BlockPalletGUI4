package org.example.bte.blockPalletGUI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class InventoryClickHandler implements Listener {

    private final BlockPalletManager blockPalletManager;

    public InventoryClickHandler(BlockPalletManager blockPalletManager) {
        this.blockPalletManager = blockPalletManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        String title = event.getView().getTitle();

        if (!title.equals("Block Pallet Menu") && !title.equals("Filter Menu")) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }
        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String displayName = meta.getDisplayName();

        if (title.equals("Block Pallet Menu")) {
            switch (displayName) {
                case "Previous Page":
                    blockPalletManager.handlePageClick(player, false);
                    break;
                case "Next Page":
                    blockPalletManager.handlePageClick(player, true);
                    break;
                case "Filter Menu":
                    new FilterMenu(blockPalletManager, player).open();
                    break;
                default:
                    break;
            }
            return;
        }

        if (title.equals("Filter Menu")) {
            if (displayName.equals("§eBack")) {
                Set<String> filters = blockPalletManager.getPlayerFilters(player);
                if (filters.isEmpty()) {
                    blockPalletManager.setPlayerFiltersAndOpen(player, "color");
                } else {
                    blockPalletManager.setPlayerFiltersAndOpen(player, filters.toArray(new String[0]));
                }
                return;
            }

            boolean startsCheck = displayName.startsWith("§a✔ ");
            boolean startsCross = displayName.startsWith("§c✘ ");
            if (startsCheck || startsCross) {
                // Normaliseer de filternaam, zodat "Concrete Powder" wordt "concrete_powder", etc.
                String filterName = displayName.substring(4).toLowerCase().replace(" ", "_");
                Set<String> filters = blockPalletManager.getPlayerFilters(player);

                if (filters.contains(filterName)) {
                    filters.remove(filterName);
                } else {
                    filters.add(filterName);
                    if (!filterName.equals("color")) {
                        filters.remove("color");
                    }
                }
                blockPalletManager.updatePlayerFilters(player, filters);

                new FilterMenu(blockPalletManager, player).open();
            }
        }
    }
}