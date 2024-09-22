package org.example.bte.blockPalletGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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


    private static final Map<Player, Integer> playerPageMap = new HashMap<>();

    public static void openBlockPalletMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "Block Pallet Menu");

        menu.setItem(0, createMenuItem(Material.STONE_SLAB, "Slabs"));
        menu.setItem(1, createMenuItem(Material.STONE_STAIRS, "Stairs"));
        menu.setItem(2, createMenuItem(Material.STONE_BRICK_WALL, "Walls"));
        menu.setItem(3, createMenuItem(Material.BEACON, "Color"));
        menu.setItem(4, createMenuItem(Material.OAK_LOG, "Logs"));
        menu.setItem(5, createMenuItem(Material.OAK_LEAVES, "Leaves"));
        menu.setItem(6, createMenuItem(Material.OAK_FENCE, "Fences"));
        menu.setItem(7, createMenuItem(Material.GLASS, "Glass"));
        menu.setItem(8, createMenuItem(Material.WHITE_CARPET, "Carpet"));
        menu.setItem(9, createMenuItem(Material.WHITE_WOOL, "Wool"));
        menu.setItem(10, createMenuItem(Material.TERRACOTTA, "Terracotta"));
        menu.setItem(11, createMenuItem(Material.WHITE_CONCRETE, "Concrete"));
        menu.setItem(12, createMenuItem(Material.WHITE_CONCRETE_POWDER, "Concrete Powder"));
        menu.setItem(13, createMenuItem(Material.RED_BED, "Bed"));
        menu.setItem(14, createMenuItem(Material.WHITE_CANDLE, "Candle"));
        menu.setItem(15, createMenuItem(Material.WHITE_BANNER, "Banner"));
        menu.setItem(16, createMenuItem(Material.WHITE_STAINED_GLASS_PANE, "Glass Pane"));

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < menu.getSize(); i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, filler);
            }
        }

        player.openInventory(menu);
    }

    public static void openBlockPalletMenu(Player player, String menuTypeReadableName, int page) {
        BlockPalletMenuType palletMenuType = BlockPalletMenuType.getMenuType(menuTypeReadableName);

        if (palletMenuType == null) {
            player.sendMessage("Invalid menu type.");
            return;
        }

        Inventory blockPalletMenu = Bukkit.createInventory(null, 27, palletMenuType.getReadableName() + " Menu");

        ItemStack[] items = palletMenuType.getItemSupplier().get();

        int startIndex = page * 25;
        int endIndex = Math.min(startIndex + 25, items.length);

        for (int i = startIndex, slot = 0; i < endIndex; i++, slot++) {
            blockPalletMenu.setItem(slot, items[i]);
        }

        if (page > 0) {
            ItemStack previousPageButton = createMenuItem(Material.ARROW, "Previous Page");
            blockPalletMenu.setItem(18, previousPageButton);
        }

        if (endIndex < items.length) {
            ItemStack nextPageButton = createMenuItem(Material.ARROW, "Next Page");
            blockPalletMenu.setItem(26, nextPageButton);
        }

        ItemStack filler = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < blockPalletMenu.getSize(); i++) {
            if (blockPalletMenu.getItem(i) == null) {
                blockPalletMenu.setItem(i, filler);
            }
        }

        player.openInventory(blockPalletMenu);
        playerPageMap.put(player, page);
    }

    private static ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static void handlePageClick(Player player, String menuTypeReadableName, boolean isNext) {
        int currentPage = playerPageMap.getOrDefault(player, 0);
        int newPage = isNext ? currentPage + 1 : Math.max(0, currentPage - 1);

        openBlockPalletMenu(player, menuTypeReadableName, newPage);
    }
}


