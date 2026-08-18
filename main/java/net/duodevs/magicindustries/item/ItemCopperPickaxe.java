package net.duodevs.magicindustries.item;

import net.minecraft.item.ItemPickaxe;
import net.duodevs.magicindustries.init.ModMaterials;
import net.duodevs.magicindustries.init.ModContent;
import net.minecraft.item.ItemStack;

public class ItemCopperPickaxe extends ItemPickaxe {
    public ItemCopperPickaxe() {
        super(ModMaterials.COPPER_TOOL);
    }
    @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return ModContent.isCopperIngot(repair) || super.getIsRepairable(toRepair, repair);
    }
}
