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

        if (event.getView().getTitle().equals("Block Pallet Menu")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType().isAir()) return;

            switch (clickedItem.getType()) {
                case STONE_SLAB:
                    BlockPalletManager.openBlockPalletMenu(player, "slabs", 0);
                    break;
                case STONE_STAIRS:
                    BlockPalletManager.openBlockPalletMenu(player, "stairs", 0);
                    break;
                case STONE_BRICK_WALL:
                    BlockPalletManager.openBlockPalletMenu(player, "walls", 0);
                    break;
                case BEACON:
                        BlockPalletManager.openBlockPalletMenu(player, "color", 0);
                    break;
                case OAK_LOG:
                    BlockPalletManager.openBlockPalletMenu(player, "logs", 0);
                    break;
                case OAK_LEAVES:
                    BlockPalletManager.openBlockPalletMenu(player, "leaves", 0);
                    break;
                case OAK_FENCE:
                    BlockPalletManager.openBlockPalletMenu(player, "fences", 0);
                    break;
                case GLASS:
                    BlockPalletManager.openBlockPalletMenu(player, "glass", 0);
                    break;
                case WHITE_CARPET:ARPET:
                    BlockPalletManager.openBlockPalletMenu(player, "carpet", 0);
                    break;
                case TERRACOTTA:
                    BlockPalletManager.openBlockPalletMenu(player, "terracotta", 0);
                    break;
                case WHITE_CONCRETE:
                    BlockPalletManager.openBlockPalletMenu(player, "concrete", 0);
                    break;
                case WHITE_CONCRETE_POWDER:
                    BlockPalletManager.openBlockPalletMenu(player, "concrete_powder", 0);
                    break;
                case RED_BED:
                    BlockPalletManager.openBlockPalletMenu(player, "bed", 0);
                    break;
                case WHITE_CANDLE:
                    BlockPalletManager.openBlockPalletMenu(player, "candle", 0);
                    break;
                case WHITE_BANNER:
                    BlockPalletManager.openBlockPalletMenu(player, "banner", 0);
                    break;
                case WHITE_STAINED_GLASS_PANE:
                    BlockPalletManager.openBlockPalletMenu(player, "glass_pane", 0);
                    break;
                case ARROW:
                    if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("Next Page")) {
                        BlockPalletManager.handlePageClick(player, menu.getTitle().replace(" Menu", "").toLowerCase(), true);
                    } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("Previous Page")) {
                        BlockPalletManager.handlePageClick(player, menu.getTitle().replace(" Menu", "").toLowerCase(), false);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}


