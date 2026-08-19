package net.duodevs.magicindustries.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class ModItemTier {
   public static final ToolMaterial COPPER = new ToolMaterial(
      BlockTags.INCORRECT_FOR_IRON_TOOL,
      200,
      5.0F,
      5.0F,
      10,
      ModArmorMaterial.COPPER_REPAIR
   );
}
