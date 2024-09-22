import org.bukkit.inventory.ItemStack;
import org.example.bte.blockPalletGUI.MenuItems;

import java.util.function.Supplier;

public enum BlockPalletMenuType {
    SLABS("slabs", MenuItems::getSlabs),
    STAIRS("stairs", MenuItems::getStairs),
    WALLS("walls", MenuItems::getWalls),
    COLOR("color", MenuItems::getBlocksByColor),
    LOGS("logs", MenuItems::getLogs),
    LEAVES("leaves", MenuItems::getLeaves),
    FENCES("fences", MenuItems::getFences),
    GLASS("glass", MenuItems::getGlass),
    CARPET("carpet", MenuItems::getCarpet),
    WOOL("wool", MenuItems::getWool),
    TERRACOTTA("terracotta", MenuItems::getTerracotta),
    CONCRETE("concrete", MenuItems::getConcrete),
    CONCRETE_POWDER("concrete_powder", MenuItems::getConcretePowder),
    BED("bed", MenuItems::getBeds),
    CANDLE("candle", MenuItems::getCandles),
    BANNER("banner", MenuItems::getBanners),
    GLASS_PANE("glass_pane", MenuItems::getGlassPanes);

    private final String readableName; // Declare these only once
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
