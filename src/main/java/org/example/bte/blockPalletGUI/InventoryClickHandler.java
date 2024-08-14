package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickHandler implements Listener {

    private final BlockPalletManager blockPalletManager;

    public InventoryClickHandler(BlockPalletManager blockPalletManager) {
        this.blockPalletManager = blockPalletManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("Pallet")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null) return;

            ItemStack currentItem = event.getCurrentItem();
            if (currentItem.getType() == XMaterial.PAPER.parseMaterial()) {
                String title = event.getView().getTitle();
                String[] parts = title.split(" ");
                try {
                    if (parts.length >= 5 && parts[4].contains("/")) {
                        String[] pageParts = parts[4].split("/");
                        int currentPage = Integer.parseInt(pageParts[0]) - 1;

                        if (currentItem.getItemMeta().getDisplayName().equals("Previous Page")) {
                            blockPalletManager.openBlockPallet(player, currentPage - 1, parts[0].toLowerCase());
                        } else if (currentItem.getItemMeta().getDisplayName().equals("Next Page")) {
                            blockPalletManager.openBlockPallet(player, currentPage + 1, parts[0].toLowerCase());
                        }
                    } else {
                        player.sendMessage("The inventory title format is incorrect.");
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    player.sendMessage("An error occurred while parsing the page number.");
                    e.printStackTrace();
                }
            }
        }
    }
}


