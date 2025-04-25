// src/main/java/org/example/bte/blockPalletGUI/BlockPalletManager.java
package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Holds per-player filters/pages and delegates
 * menu-building to BlockListMenu.open(...)
 */
public class BlockPalletManager {
    // made public so other classes (InventoryClickHandler, FilterMenu) can use this
    public static final int PAGE_SIZE = 36;

    private final Map<UUID, List<String>> playerFilterMap = new HashMap<>();
    private final Map<UUID, Integer>     playerPageMap   = new HashMap<>();

    public static final String HEAD_BETWEEN_ARROWS =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6"
                    + "Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv"
                    + "MmRjYTYwZTRlMjNjNjI3OTc5YWJiMjZmMjhiYjkxODNh"
                    + "ZThjMmM2ZmViZTU0YjNjODliOGZjNDYzZjNhNDAwNSJ9"
                    + "fX0=";

    public static final String LEFT_ARROW =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6"
                    + "Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv"
                    + "Y2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVl"
                    + "YjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19";

    public static final String RIGHT_ARROW =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6"
                    + "Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv"
                    + "OTU2YTM2MTg0NTllNDNiMjg3YjIyYjdlMjM1ZWM2OTk1"
                    + "OTQ1NDZjNmZjZDZkYzg0YmZjYTRjZjMwYWI5MzExIn19fQ==";

    public BlockPalletManager() {}

    /** Main entry point to open the block menu */
    public void openBlockMenu(Player player) {
        BlockListMenu.open(this, player);
    }

    /** Used by InventoryClickHandler to switch pages */
    public void handlePageClick(Player player, boolean isNext) {
        int current = playerPageMap.getOrDefault(player.getUniqueId(), 0);
        current += isNext ? 1 : -1;
        current = Math.max(current, 0);
        playerPageMap.put(player.getUniqueId(), current);
        openBlockMenu(player);
    }

    /** Retrieves the current filters (FilterMenu) */
    public Set<String> getPlayerFilters(Player player) {
        return new HashSet<>(
                playerFilterMap.getOrDefault(player.getUniqueId(), Collections.emptyList())
        );
    }

    /** Updates and keeps track of filters (FilterMenu) */
    public void updatePlayerFilters(Player player, Set<String> filters) {
        playerFilterMap.put(player.getUniqueId(), new ArrayList<>(filters));
    }

    /**
     * Convenient to set filters immediately and reopen the menu
     * (e.g., after a click in FilterMenu).
     */
    public void setPlayerFiltersAndOpen(Player player, String... filters) {
        if (filters == null || filters.length == 0) {
            playerFilterMap.put(player.getUniqueId(),
                    Collections.singletonList("color"));
        } else {
            playerFilterMap.put(player.getUniqueId(),
                    Arrays.asList(filters));
        }
        playerPageMap.put(player.getUniqueId(), 0);
        openBlockMenu(player);
    }

    // ─── package-private helpers for BlockListMenu.open(...) ───

    int getPlayerPage(Player player) {
        return playerPageMap.getOrDefault(player.getUniqueId(), 0);
    }
    void setPlayerPage(Player player, int page) {
        playerPageMap.put(player.getUniqueId(), page);
    }

    List<String> getFilters(Player player) {
        return playerFilterMap.getOrDefault(player.getUniqueId(),
                Collections.singletonList("color"));
    }

    ItemStack[] getItemsForFilters(List<String> filters) {
        List<ItemStack> combined = new ArrayList<>();
        for (String filter : filters) {
            combined.addAll(Arrays.asList(MenuItems.getItemsByFilter(filter)));
        }
        return combined.toArray(new ItemStack[0]);
    }

    ItemStack createMenuItem(XMaterial material, String name) {
        ItemStack item = material.parseItem();
        if (item == null) item = new ItemStack(Material.BARRIER);
        ItemMeta m = item.getItemMeta();
        if (m != null) {
            m.setDisplayName(name);
            item.setItemMeta(m);
        }
        return item;
    }

    ItemStack createCustomHeadBase64(String base64, String displayName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        skull = Bukkit.getUnsafe().modifyItemStack(
                skull,
                "{SkullOwner:{Id:\"" + UUID.randomUUID() +
                        "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
        );
        ItemMeta m = skull.getItemMeta();
        if (m != null) {
            m.setDisplayName(displayName);
            skull.setItemMeta(m);
        }
        return skull;
    }
}
