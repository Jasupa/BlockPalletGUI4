package org.example.bte.blockPalletGUI;

import org.bukkit.inventory.ItemStack;
import java.util.function.Supplier;

public enum BlockPalletMenuType {

    SLABS("slabs", MenuItems::getSlabs),
    STAIRS("stairs", MenuItems::getStairs),
    WALLS("walls", MenuItems::getWalls);

    private final String readableName;
    private final Supplier<ItemStack[]> itemSupplier;

    BlockPalletMenuType(String readableName, Supplier<ItemStack[]> itemSupplier) {
        this.readableName = readableName;
        this.itemSupplier = itemSupplier;
    }

    public String getReadableName() {
        return readableName;
    }

    public Supplier<ItemStack[]> getItemSupplier() {
        return itemSupplier;
    }

    public static BlockPalletMenuType getMenuType(String readableName) {
        for (BlockPalletMenuType menuType : BlockPalletMenuType.values()) {
            if (menuType.getReadableName().equalsIgnoreCase(readableName)) {
                return menuType;
            }
        }
        return null;
    }
}
