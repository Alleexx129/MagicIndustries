package net.duodevs.magicindustries.item;

import net.minecraft.item.ItemAxe;
import net.duodevs.magicindustries.init.ModMaterials;
import net.duodevs.magicindustries.init.ModContent;
import net.minecraft.item.ItemStack;

public class ItemCopperAxe extends ItemAxe {
    public ItemCopperAxe() {
        // 1.20.1 constructor parameters were +6 attack modifier and -3.1 speed.
        super(ModMaterials.COPPER_TOOL, 6.0F, -3.1F);
    }
    @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return ModContent.isCopperIngot(repair) || super.getIsRepairable(toRepair, repair);
    }
}
