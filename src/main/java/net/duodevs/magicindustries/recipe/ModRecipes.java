package net.duodevs.magicindustries.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
   public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "magicindustries");
   public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, "magicindustries");

   public static final RegistryObject<RecipeSerializer<ManaExtractorRecipe>> MANA_EXTRACTOR_SERIALIZER = SERIALIZERS.register(
      "gem_infusing", () -> ManaExtractorRecipe.Serializer.INSTANCE
   );
   public static final RegistryObject<RecipeType<ManaExtractorRecipe>> MANA_EXTRACTOR_TYPE = TYPES.register(
      "gem_infusing", () -> new RecipeType<>() {
         public String toString() {
            return "magicindustries:gem_infusing";
         }
      }
   );

   public static void register(IEventBus eventBus) {
      SERIALIZERS.register(eventBus);
      TYPES.register(eventBus);
   }
}
