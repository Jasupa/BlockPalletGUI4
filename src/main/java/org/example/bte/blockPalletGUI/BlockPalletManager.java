package org.example.bte.blockPalletGUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.cryptomorin.xseries.XMaterial;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockPalletManager {

    private static final int PAGE_SIZE = 45;

    public void openBlockPallet(Player player, int page, String type) {
        // Define the mapping between type and item retrieval method
        Map<String, Supplier<ItemStack[]>> itemTypeMap = new HashMap<>();
        itemTypeMap.put("slabs", MenuItems::getSlabs);
        itemTypeMap.put("stairs", MenuItems::getStairs);
        itemTypeMap.put("walls", MenuItems::getWalls);
        itemTypeMap.put("logs", MenuItems::getLogs);
        itemTypeMap.put("leaves", MenuItems::getLeaves);
        itemTypeMap.put("fences", MenuItems::getFences);
        itemTypeMap.put("glass", MenuItems::getGlass);
        itemTypeMap.put("carpet", MenuItems::getCarpet);
        itemTypeMap.put("wool", MenuItems::getWool);
        itemTypeMap.put("terracotta", MenuItems::getTerracotta);
        itemTypeMap.put("concrete", MenuItems::getConcrete);
        itemTypeMap.put("concrete_powder", MenuItems::getConcretePowder);
        itemTypeMap.put("bed", MenuItems::getBeds);
        itemTypeMap.put("candle", MenuItems::getCandles);
        itemTypeMap.put("banner", MenuItems::getBanners);
        itemTypeMap.put("glass_pane", MenuItems::getGlassPanes);

        Supplier<ItemStack[]> itemSupplier = itemTypeMap.getOrDefault(type.toLowerCase(), MenuItems::getBlocksByColor);
        ItemStack[] items = itemSupplier.get();


        int totalPages = (int) Math.ceil((double) items.length / PAGE_SIZE);
        String formattedType = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        String title = String.format("%s Pallet - Page %d/%d", formattedType, page + 1, totalPages);
        Inventory gui = Bukkit.createInventory(player, 54, title);

        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, items.length);
        for (int i = startIndex; i < endIndex; i++) {
            gui.setItem(i - startIndex, items[i]);
        }

        if (page > 0) {
            gui.setItem(45, createNavigationItem("Previous Page", "Click to go to the previous page"));
        }
        if (page < totalPages - 1) {
            gui.setItem(53, createNavigationItem("Next Page", "Click to go to the next page"));
        }

        player.openInventory(gui);
    }

    private ItemStack createNavigationItem(String name, String lore) {
        ItemStack item = XMaterial.PAPER.parseItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Collections.singletonList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}


