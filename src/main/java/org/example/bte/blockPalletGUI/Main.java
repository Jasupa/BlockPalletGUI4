package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public final class Main extends JavaPlugin implements CommandExecutor, Listener {


    private static final int PAGE_SIZE = 45; // 54 total - 9 for navigation

    @Override
    public void onEnable() {
        getLogger().info("Block Pallet GUI has started successfully");

        // Register the command executor for 'blockpallet' and its subcommands
        if (getCommand("blockpallet") != null) {
            getCommand("blockpallet").setExecutor(this);
        } else {
            getLogger().severe("Command 'blockpallet' not found in plugin.yml");
        }

        // Register event listener
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Block Pallet GUI has stopped");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0 && args[0].equalsIgnoreCase("color")) {
                openBlockPallet(player, 0, "color");
                return true;
            } else if (args.length > 0 && args[0].equalsIgnoreCase("slabs")) {
                openBlockPallet(player, 0, "slabs");
                return true;
            } else if (args.length > 0 && args[0].equalsIgnoreCase("stairs")) {
                openBlockPallet(player, 0, "stairs");
                return true;
            } else if (args.length > 0 && args[0].equalsIgnoreCase("walls")) {
                openBlockPallet(player, 0, "walls");
                return true;
            } else {
                player.sendMessage("Usage: /blockpallet color, /blockpallet slabs, /blockpallet stairs, or /blockpallet walls");
                return true;
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
        }
        return false;
    }

    private void openBlockPallet(Player player, int page, String type) {
        // Retrieve the appropriate blocks based on the command
        ItemStack[] items;
        switch (type.toLowerCase()) {
            case "slabs":
                items = MenuItems.getSlabs();
                break;
            case "stairs":
                items = MenuItems.getStairs();
                break;
            case "walls":
                items = MenuItems.getWalls();
                break;
            default:
                items = MenuItems.getBlocksByColor();
        }

        int totalPages = (int) Math.ceil((double) items.length / PAGE_SIZE);

        // Create an inventory with size 54 (6 rows of 9)
        String title = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase() + " Pallet - Page " + (page + 1) + "/" + totalPages;
        Inventory gui = Bukkit.createInventory(null, 54, title);

        // Populate the inventory with items for the current page
        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, items.length);
        for (int i = startIndex; i < endIndex; i++) {
            gui.setItem(i - startIndex, items[i]);
        }

        // Add navigation buttons
        if (page > 0) {
            gui.setItem(45, createNavigationItem("Previous Page", "Click to go to the previous page"));
        }
        if (page < totalPages - 1) {
            gui.setItem(53, createNavigationItem("Next Page", "Click to go to the next page"));
        }

        // Open the inventory for the player
        player.openInventory(gui);
    }

    private ItemStack createNavigationItem(String name, String lore) {
        // Create an item stack with navigation properties (e.g., a special item like an arrow or paper)
        // Here we are using Paper as an example
        ItemStack item = XMaterial.PAPER.parseItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Collections.singletonList(lore));
            item.setItemMeta(meta);
        }
        return item;
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
                    // Check if the title has enough parts to include "Page X/Y"
                    if (parts.length >= 5 && parts[4].contains("/")) {
                        String[] pageParts = parts[4].split("/");
                        int currentPage = Integer.parseInt(pageParts[0]) - 1;

                        if (currentItem.getItemMeta().getDisplayName().equals("Previous Page")) {
                            openBlockPallet(player, currentPage - 1, parts[0].toLowerCase());
                        } else if (currentItem.getItemMeta().getDisplayName().equals("Next Page")) {
                            openBlockPallet(player, currentPage + 1, parts[0].toLowerCase());
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
