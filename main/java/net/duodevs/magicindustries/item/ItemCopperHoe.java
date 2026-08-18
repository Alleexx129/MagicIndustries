package net.duodevs.magicindustries.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.duodevs.magicindustries.init.ModContent;
import net.duodevs.magicindustries.init.ModMaterials;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

public class ItemCopperHoe extends ItemHoe {
    public ItemCopperHoe() { super(ModMaterials.COPPER_TOOL); }

    /** 1.20 source: HoeItem(COPPER, -2, -1.0); tier damage bonus 5 => +3 attack-damage attribute. */
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        if (slot != EntityEquipmentSlot.MAINHAND) return super.getItemAttributeModifiers(slot);
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
            new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 3.0D, 0));
        map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
            new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.0D, 0));
        return map;
    }

    @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return ModContent.isCopperIngot(repair) || super.getIsRepairable(toRepair, repair);
    }
}
