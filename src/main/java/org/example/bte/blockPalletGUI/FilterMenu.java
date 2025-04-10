package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.type.ChestMenu;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FilterMenu {
    private final BlockPalletManager manager;

    private static final String LEFT_ARROW =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90" +
                    "ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0" +
                    "ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYx" +
                    "YzQyMzYyMTQyYmFlMWVkZDUifX19";

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

    public FilterMenu(BlockPalletManager manager) {
        this.manager = manager;
    }

    public void open(Player player) {
        Menu menu = ChestMenu.builder(5)
                .title("Filter Menu")
                .build();

        Mask mask = BinaryMask.builder(menu)
                .item(createItem(XMaterial.GRAY_STAINED_GLASS_PANE, " "))
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .build();
        mask.apply(menu);

        Set<String> currentFilters = manager.getPlayerFilters(player);
        int slot = 10;
        for (Map.Entry<String, XMaterial> entry : FILTERS.entrySet()) {
            String filterName = entry.getKey();
            boolean active = currentFilters.contains(filterName);

            String prefix = active ? "§a✔ " : "§c✘ ";
            String displayName = prefix + capitalize(filterName);
            ItemStack filterItem = createItem(entry.getValue(), displayName);

            menu.getSlot(slot).setItem(filterItem);

            final String capturedFilterName = filterName;
            menu.getSlot(slot).setClickHandler((clickPlayer, clickInfo) -> {
                Set<String> filters = manager.getPlayerFilters(clickPlayer);
                if (filters.contains(capturedFilterName)) {
                    filters.remove(capturedFilterName);
                } else {
                    filters.add(capturedFilterName);
                }
                manager.updatePlayerFilters(clickPlayer, filters);
                open(clickPlayer); // Refresh menu
            });

            slot++;
            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }
            if (slot >= 36) break;
        }

        ItemStack backItem = manager.createCustomHeadBase64(LEFT_ARROW, "§eBack");
        menu.getSlot(36).setItem(backItem);
        menu.getSlot(36).setClickHandler((p, info) -> {
            manager.openBlockMenu(p);
        });

        menu.open(player);
    }

    private ItemStack createItem(XMaterial mat, String displayName) {
        ItemStack item = mat.parseItem();
        if (item == null) {
            item = new ItemStack(XMaterial.BARRIER.parseMaterial());
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            item.setItemMeta(meta);
        }
        return item;
    }

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
}