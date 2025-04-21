package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.type.ChestMenu;

import java.util.*;

public class BlockPalletManager {
    private static final int PAGE_SIZE = 36;

    private final Map<UUID, List<String>> playerFilterMap = new HashMap<>();
    private final Map<UUID, Integer> playerPageMap = new HashMap<>();

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

    public BlockPalletManager() {
    }

    public void openBlockMenu(Player player) {
        List<String> filters = playerFilterMap.getOrDefault(player.getUniqueId(), Collections.singletonList("color"));
        final int currentPage = playerPageMap.getOrDefault(player.getUniqueId(), 0);

        ItemStack[] items = getItemsForFilters(filters);
        int totalPages = Math.max((int) Math.ceil((double) items.length / PAGE_SIZE), 1);

        final int page;
        if (currentPage >= totalPages) {
            page = Math.max(0, totalPages - 1);
        } else {
            page = currentPage;
        }
        playerPageMap.put(player.getUniqueId(), page);

        Menu menu = ChestMenu.builder(6)
                .title("Block Pallet Menu")
                .build();

        Mask backgroundMask = BinaryMask.builder(menu)
                .item(createMenuItem(XMaterial.GRAY_STAINED_GLASS_PANE, " "))
                .pattern("111111111")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("111111111")
                .build();
        backgroundMask.apply(menu);

        ItemStack filterMenuItem = createMenuItem(XMaterial.HOPPER, "Filter Menu");
        menu.getSlot(4).setItem(filterMenuItem);
        menu.getSlot(4).setClickHandler((p, info) -> new FilterMenu(this, p).open());

        ItemStack prevButton = createCustomHeadBase64(LEFT_ARROW, "Previous Page");
        menu.getSlot(48).setItem(prevButton);
        menu.getSlot(48).setClickHandler((p, info) -> {
            if (page > 0) {
                playerPageMap.put(p.getUniqueId(), page - 1);
                openBlockMenu(p);
            }
        });

        String pageText = (page + 1) + "/" + totalPages;
        menu.getSlot(49).setItem(createCustomHeadBase64(HEAD_BETWEEN_ARROWS, pageText));

        ItemStack nextButton = createCustomHeadBase64(RIGHT_ARROW, "Next Page");
        menu.getSlot(50).setItem(nextButton);
        menu.getSlot(50).setClickHandler((p, info) -> {
            if (page < totalPages - 1) {
                playerPageMap.put(p.getUniqueId(), page + 1);
                openBlockMenu(p);
            }
        });

        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, items.length);
        for (int i = startIndex; i < endIndex; i++) {
            final int slotIndex = 9 + (i - startIndex);
            if (slotIndex >= 45)
                break;
            final int itemIndex = i;
            menu.getSlot(slotIndex).setItem(items[itemIndex]);
            menu.getSlot(slotIndex).setClickHandler((p, info) -> p.getInventory().addItem(items[itemIndex].clone()));
        }

        menu.open(player);
    }

    public void handlePageClick(Player player, boolean isNext) {
        int currentPage = playerPageMap.getOrDefault(player.getUniqueId(), 0);
        currentPage += (isNext ? 1 : -1);
        currentPage = Math.max(currentPage, 0);
        playerPageMap.put(player.getUniqueId(), currentPage);
        openBlockMenu(player);
    }

    public void setPlayerFiltersAndOpen(Player player, String... filters) {
        if (filters == null || filters.length == 0) {
            playerFilterMap.put(player.getUniqueId(), Collections.singletonList("color"));
        } else {
            playerFilterMap.put(player.getUniqueId(), Arrays.asList(filters));
        }
        playerPageMap.put(player.getUniqueId(), 0);
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
                "{SkullOwner:{Id:\"" + UUID.randomUUID() + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
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
        return new HashSet<>(playerFilterMap.getOrDefault(player.getUniqueId(), Collections.emptyList()));
    }

    public void updatePlayerFilters(Player player, Set<String> filters) {
        playerFilterMap.put(player.getUniqueId(), new ArrayList<>(filters));
    }
}