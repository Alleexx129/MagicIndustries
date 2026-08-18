package net.duodevs.magicindustries.init;

import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public final class ModMaterials {
    private ModMaterials() {}

    // Exact 1.20.1 tier values: level 2, durability 200, speed 5, attack bonus 5, enchantability 10.
    public static final Item.ToolMaterial COPPER_TOOL =
        EnumHelper.addToolMaterial("MAGICINDUSTRIES_COPPER", 2, 200, 5.0F, 5.0F, 10);

    // Exact 1.20.1 armor values: durability multiplier 10, defenses boots/legs/chest/head = 1/3/4/2, enchantability 12.
    public static final ItemArmor.ArmorMaterial COPPER_ARMOR =
        EnumHelper.addArmorMaterial(
            "MAGICINDUSTRIES_COPPER",
            MagicIndustries.MODID + ":copper",
            10,
            new int[]{1, 3, 4, 2},
            12,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F
        );
}
