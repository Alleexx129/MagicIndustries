package net.duodevs.magicindustries.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MagicIndustriesTags {
   public static class Items {
      public static final TagKey<Item> GEMSTONE_SAW = createForgeTag("tools/gemstone_saw");

      private static TagKey<Item> createTag(String name) {
         return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("magicindustries", name));
      }

      private static TagKey<Item> createForgeTag(String name) {
         return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("forge", name));
      }
   }
}
