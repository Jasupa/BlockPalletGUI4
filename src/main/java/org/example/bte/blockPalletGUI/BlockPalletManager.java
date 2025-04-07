package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BlockPalletManager {
    private static final int PAGE_SIZE = 36;

    private final Map<Player, List<String>> playerFilterMap = new HashMap<>();
    private final Map<Player, Integer> playerPageMap = new HashMap<>();

    private static final String HEAD_BETWEEN_ARROWS =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6" +
                    "Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" +
                    "MmRjYTYwZTRlMjNjNjI3OTc5YWJiMjZmMjhiYjkxODNh" +
                    "ZThjMmM2ZmViZTU0YjNjODliOGZjNDYzZjNhNDAwNSJ9" +
                    "fX0=";

    private static final String LEFT_ARROW =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6" +
                    "Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" +
                    "Y2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVl" +
                    "YjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19";

    private static final String RIGHT_ARROW =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6" +
                    "Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" +
                    "OTU2YTM2MTg0NTllNDNiMjg3YjIyYjdlMjM1ZWM2OTk1" +
                    "OTQ1NDZjNmZjZDZkYzg0YmZjYTRjZjMwYWI5MzExIn19fQ==";

    public void openBlockMenu(Player player) {
        List<String> filters = playerFilterMap.getOrDefault(player, Collections.singletonList("color"));
        int page = playerPageMap.getOrDefault(player, 0);

        ItemStack[] items = getItemsForFilters(filters);

        int totalPages = Math.max((int) Math.ceil((double) items.length / PAGE_SIZE), 1);
        if (page >= totalPages) {
            page = totalPages - 1;
        }
        page = Math.max(page, 0);
        playerPageMap.put(player, page);

        Inventory inv = Bukkit.createInventory(null, 6 * 9, "Block Pallet Menu");

        ItemStack filler = createMenuItem(XMaterial.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, filler);
        }
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, filler);
        }

        ItemStack filterMenuItem = createMenuItem(XMaterial.HOPPER, "Filter Menu");
        inv.setItem(4, filterMenuItem);

        inv.setItem(48, createCustomHeadBase64(LEFT_ARROW, "Previous Page"));

        String pageText = (page + 1) + "/" + totalPages;
        inv.setItem(49, createCustomHeadBase64(HEAD_BETWEEN_ARROWS, pageText));

        inv.setItem(50, createCustomHeadBase64(RIGHT_ARROW, "Next Page"));

        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, items.length);
        int slotIndex = 9;
        for (int i = startIndex; i < endIndex; i++) {
            inv.setItem(slotIndex++, items[i]);
        }

        player.openInventory(inv);
    }

    public void handlePageClick(Player player, boolean isNext) {
        int currentPage = playerPageMap.getOrDefault(player, 0);
        currentPage += (isNext ? 1 : -1);
        currentPage = Math.max(currentPage, 0); // don’t go negative
        playerPageMap.put(player, currentPage);
        openBlockMenu(player);
    }

    public void setPlayerFiltersAndOpen(Player player, String... filters) {
        if (filters == null || filters.length == 0) {
            playerFilterMap.put(player, Collections.singletonList("color"));
        } else {
            playerFilterMap.put(player, Arrays.asList(filters));
        }
        playerPageMap.put(player, 0);
        openBlockMenu(player);
    }

    public ItemStack createMenuItem(XMaterial material, String name) {
        ItemStack item = material.parseItem();
        if (item == null) {
            item = new ItemStack(Material.BARRIER);
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack createCustomHeadBase64(String base64, String displayName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        skull = Bukkit.getUnsafe().modifyItemStack(
                skull,
                "{SkullOwner:{Id:\""+UUID.randomUUID()+"\",Properties:{textures:[{Value:\""+base64+"\"}]}}}"
        );
        ItemMeta meta = skull.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            skull.setItemMeta(meta);
        }
        return skull;
    }

    private ItemStack[] getItemsForFilters(List<String> filters) {
        List<ItemStack> combined = new ArrayList<>();
        for (String filter : filters) {
            ItemStack[] arr = MenuItems.getItemsByFilter(filter);
            combined.addAll(Arrays.asList(arr));
        }
        return combined.toArray(new ItemStack[0]);
    }

    public Set<String> getPlayerFilters(Player player) {
        return new HashSet<>(playerFilterMap.getOrDefault(player, Collections.emptyList()));
    }

    public void updatePlayerFilters(Player player, Set<String> filters) {
        playerFilterMap.put(player, new ArrayList<>(filters));
    }
}
