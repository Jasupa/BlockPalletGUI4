// BlockListMenu.java
package org.example.bte.blockPalletGUI;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.type.ChestMenu;

import java.util.Collections;
import java.util.List;

/**
 * Encapsulates everything that was in openBlockMenu(...) so
 * BlockPalletManager is just a thin delegate.
 */
public class BlockListMenu {

    public static void open(BlockPalletManager manager, Player player) {
        // fetch filters & page
        List<String> filters = manager.getFilters(player);
        if (filters.isEmpty()) {
            filters = Collections.singletonList("color");
        }
        int currentPage = manager.getPlayerPage(player);

        // build items & compute total pages
        ItemStack[] items = manager.getItemsForFilters(filters);
        int totalPages = Math.max(
                (int)Math.ceil((double) items.length / BlockPalletManager.PAGE_SIZE),
                1
        );

        int page = (currentPage >= totalPages)
                ? Math.max(0, totalPages - 1)
                : currentPage;
        manager.setPlayerPage(player, page);

        // create the menu
        Menu menu = ChestMenu.builder(6)
                .title("Block Pallet Menu")
                .build();

        // background border
        Mask backgroundMask = BinaryMask.builder(menu)
                .item(manager.createMenuItem(XMaterial.GRAY_STAINED_GLASS_PANE, " "))
                .pattern("111111111")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("111111111")
                .build();
        backgroundMask.apply(menu);

        // filter button
        ItemStack filterMenuItem = manager.createMenuItem(XMaterial.HOPPER, "Filter Menu");
        menu.getSlot(4).setItem(filterMenuItem);
        menu.getSlot(4).setClickHandler((p, info) ->
                new FilterMenu(manager, p).open()
        );

        // previous page arrow
        ItemStack prevButton =
                manager.createCustomHeadBase64(BlockPalletManager.LEFT_ARROW, "Previous Page");
        menu.getSlot(48).setItem(prevButton);
        menu.getSlot(48).setClickHandler((p, info) -> {
            if (page > 0) {
                manager.setPlayerPage(p, page - 1);
                manager.openBlockMenu(p);
            }
        });

        // page indicator
        String pageText = (page + 1) + "/" + totalPages;
        menu.getSlot(49).setItem(
                manager.createCustomHeadBase64(BlockPalletManager.HEAD_BETWEEN_ARROWS, pageText)
        );

        // next page arrow
        ItemStack nextButton =
                manager.createCustomHeadBase64(BlockPalletManager.RIGHT_ARROW, "Next Page");
        menu.getSlot(50).setItem(nextButton);
        menu.getSlot(50).setClickHandler((p, info) -> {
            if (page < totalPages - 1) {
                manager.setPlayerPage(p, page + 1);
                manager.openBlockMenu(p);
            }
        });

        // fill block slots 9–44
        int startIndex = page * BlockPalletManager.PAGE_SIZE;
        int endIndex   = Math.min(startIndex + BlockPalletManager.PAGE_SIZE, items.length);
        for (int i = startIndex; i < endIndex; i++) {
            int slotIndex = 9 + (i - startIndex);
            if (slotIndex >= 45) break;
            final ItemStack item = items[i];
            menu.getSlot(slotIndex).setItem(item);
            menu.getSlot(slotIndex).setClickHandler((p, info) ->
                    p.getInventory().addItem(item.clone())
            );
        }

        // open it up
        menu.open(player);
    }
}
