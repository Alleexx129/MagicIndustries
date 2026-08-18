package net.duodevs.magicindustries.item;

import net.duodevs.magicindustries.init.ModContent;
import net.duodevs.magicindustries.init.ModMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemCopperSword extends ItemSword {
    public ItemCopperSword() { super(ModMaterials.COPPER_TOOL); }
    @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return ModContent.isCopperIngot(repair) || super.getIsRepairable(toRepair, repair);
    }
}
