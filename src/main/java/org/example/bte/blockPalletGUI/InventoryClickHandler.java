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

        // Controleer of het hoofdmenu wordt geopend
        if (event.getView().getTitle().equals("Block Pallet Menu")) {
            event.setCancelled(true); // Voorkom het verplaatsen van items

            // Controleer of het aangeklikte item niet null of lucht is
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType().isAir()) {
                return; // Als er geen item is aangeklikt, verlaat de methode
            }

            // Open het juiste submenu op basis van het aangeklikte item
            switch (clickedItem.getType()) {
                case STONE_SLAB:
                    // Open het Slabs menu
                    BlockPalletManager.openBlockPalletMenu(player, "slabs");
                    break;
                case STONE_STAIRS:
                    // Open het Stairs menu
                    BlockPalletManager.openBlockPalletMenu(player, "stairs");
                    break;
                case STONE_BRICK_WALL:
                    // Open het Walls menu
                    BlockPalletManager.openBlockPalletMenu(player, "walls");
                    break;
                default:
                    break;
            }
        }
    }




}


