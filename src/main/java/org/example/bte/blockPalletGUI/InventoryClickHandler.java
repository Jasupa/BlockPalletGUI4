package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickHandler implements Listener {

    private final BlockPalletManager blockPalletManager;

    public InventoryClickHandler(BlockPalletManager blockPalletManager) {
        this.blockPalletManager = blockPalletManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryTitle = event.getView().getTitle();

        if (!inventoryTitle.contains("Pallet")) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();

        if (!(currentItem.getType() == XMaterial.PAPER.parseMaterial())) return;
        String[] splitInventoryTitle = inventoryTitle.split(" ");

        try {
            if (splitInventoryTitle.length >= 5 && splitInventoryTitle[4].contains("/")) {
                String[] pageParts = splitInventoryTitle[4].split("/");

                int currentPage = Integer.parseInt(pageParts[0]) - 1;
                String itemName = currentItem.getItemMeta().getDisplayName();
                int newPage = currentPage;

                if ("Previous Page".equals(itemName)) {
                    newPage -= 1;
                } else if ("Next Page".equals(itemName)) {
                    newPage += 1;
                }
                blockPalletManager.openBlockPallet(player, newPage, splitInventoryTitle[0].toLowerCase());
            } else {
                player.sendMessage("The inventory title format is incorrect.");
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            player.sendMessage("An error occurred while parsing the page number.");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Inventory menu = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        // Check if it's the Block Pallet Menu
        if (event.getView().getTitle().equals("Block Pallet Menu")) {
            event.setCancelled(true); // Prevent item movement

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType().isAir()) return;

            // Open the corresponding submenu based on the clicked item
            switch (clickedItem.getType()) {
                case STONE_SLAB:
                    BlockPalletManager.openBlockPalletMenu(player, "slabs"); // Open slabs menu
                    break;
                case STONE_STAIRS:
                    BlockPalletManager.openBlockPalletMenu(player, "stairs"); // Open stairs menu
                    break;
                case STONE_BRICK_WALL:
                    BlockPalletManager.openBlockPalletMenu(player, "walls");  // Open walls menu
                    break;
                default:
                    break;
            }
        }
    }





}


