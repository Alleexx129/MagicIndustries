package net.duodevs.magicindustries.item;

import net.duodevs.magicindustries.init.ModContent;
import net.duodevs.magicindustries.init.ModMaterials;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemCopperArmor extends ItemArmor {
    public ItemCopperArmor(EntityEquipmentSlot slot) {
        super(ModMaterials.COPPER_ARMOR, 0, slot);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return ModContent.isCopperIngot(repair) || super.getIsRepairable(toRepair, repair);
    }
}
