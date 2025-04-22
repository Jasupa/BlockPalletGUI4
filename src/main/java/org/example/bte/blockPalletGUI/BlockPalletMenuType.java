// src/main/java/org/example/bte/blockPalletGUI/BlockPalletMenuType.java
package org.example.bte.blockPalletGUI;

import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public enum BlockPalletMenuType {
    SLABS("Slabs", MenuItems::getSlabs),
    STAIRS("Stairs", MenuItems::getStairs),
    WALLS("Walls", MenuItems::getWalls),
    COLOR("Color", MenuItems::getBlocksByColor),
    LOGS("Logs", MenuItems::getLogs),
    LEAVES("Leaves", MenuItems::getLeaves),
    FENCES("Fences", MenuItems::getFences),
    GLASS("Glass", MenuItems::getGlass),
    CARPET("Carpet", MenuItems::getCarpet),
    WOOL("Wool", MenuItems::getWool),
    TERRACOTTA("Terracotta", MenuItems::getTerracotta),
    CONCRETE("Concrete", MenuItems::getConcrete),
    CONCRETE_POWDER("Concrete Powder", MenuItems::getConcretePowder),
    BED("Bed", MenuItems::getBeds),
    CANDLE("Candle", MenuItems::getCandles),
    BANNER("Banner", MenuItems::getBanners),
    GLASS_PANE("Glass Pane", MenuItems::getGlassPanes);

    private final String readableName;
    private final Supplier<ItemStack[]> itemSupplier;

    private static final Map<String, BlockPalletMenuType> nameToType = new HashMap<>();

    static {
        for (BlockPalletMenuType type : values()) {
            nameToType.put(type.getReadableName().toLowerCase(), type);
        }
    }

    /**
     * Complete list of all supported filter keys (for tab completion).
     */
    public static final List<String> FILTER_OPTIONS =
            Collections.unmodifiableList(Arrays.asList(
                    "color", "slabs", "stairs", "walls", "logs", "leaves", "fences",
                    "carpet", "wool", "terracotta", "concrete", "concrete_powder",
                    "bed", "candle", "banner", "glass_pane", "signs", "shulker_boxes",
                    "gates", "glass"
            ));

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
        return nameToType.get(readableName.toLowerCase());
    }
}
