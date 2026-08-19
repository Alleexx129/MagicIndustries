package net.duodevs.magicindustries.util;

import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MagicIndustriesTags {
    private MagicIndustriesTags() {}

    public static final class Items {
        private Items() {}
        public static final TagKey<Item> GEMSTONE_SAWS = createTag("gemstone_saws");
        public static final TagKey<Item> MANA_FILTERS = createTag("mana_filters");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, name));
        }
    }
}
