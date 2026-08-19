package net.duodevs.magicindustries.recipe;

import java.util.function.Supplier;
import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
    private ModRecipes() {}

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MagicIndustries.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MagicIndustries.MOD_ID);

    public static final Supplier<RecipeSerializer<ManaExtractorRecipe>> MANA_EXTRACTOR_SERIALIZER =
            SERIALIZERS.register("gem_infusing", () -> ManaExtractorRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<ManaExtractorRecipe>> MANA_EXTRACTOR_TYPE = TYPES.register(
            "gem_infusing",
            () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return MagicIndustries.MOD_ID + ":gem_infusing";
                }
            }
    );

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
