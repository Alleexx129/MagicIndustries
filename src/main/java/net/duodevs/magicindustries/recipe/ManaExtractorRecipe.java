package net.duodevs.magicindustries.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class ManaExtractorRecipe implements Recipe<SingleRecipeInput> {
   private final ItemStack output;
   private final NonNullList<Ingredient> recipeItems;
   private final FluidStack fluidStack;

   public ManaExtractorRecipe(ItemStack output, List<Ingredient> recipeItems, FluidStack fluidStack) {
      this.output = output;
      this.recipeItems = NonNullList.create();
      this.recipeItems.addAll(recipeItems);
      this.fluidStack = fluidStack;
   }

   public boolean matches(SingleRecipeInput pInput, Level pLevel) {
      return !pLevel.isClientSide() && !this.recipeItems.isEmpty() && this.recipeItems.get(0).test(pInput.getItem(0));
   }

   public ItemStack assemble(SingleRecipeInput pInput, HolderLookup.Provider pRegistries) {
      return this.output.copy();
   }

   public FluidStack getFluid() {
      return this.fluidStack;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.recipeItems;
   }

   public boolean canCraftInDimensions(int pWidth, int pHeight) {
      return true;
   }

   public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
      return this.output.copy();
   }

   public RecipeSerializer<?> getSerializer() {
      return ModRecipes.MANA_EXTRACTOR_SERIALIZER.get();
   }

   public RecipeType<?> getType() {
      return ModRecipes.MANA_EXTRACTOR_TYPE.get();
   }

   public static class Serializer implements RecipeSerializer<ManaExtractorRecipe> {
      public static final ManaExtractorRecipe.Serializer INSTANCE = new ManaExtractorRecipe.Serializer();

      public static final MapCodec<ManaExtractorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
         ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
         Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.recipeItems),
         FluidStack.CODEC.fieldOf("fluid").forGetter(ManaExtractorRecipe::getFluid)
      ).apply(instance, ManaExtractorRecipe::new));

      public static final StreamCodec<RegistryFriendlyByteBuf, ManaExtractorRecipe> STREAM_CODEC = StreamCodec.of(
         (buf, recipe) -> {
            buf.writeVarInt(recipe.recipeItems.size());
            buf.writeNbt(recipe.fluidStack.writeToNBT(new CompoundTag()));

            for (Ingredient ingredient : recipe.recipeItems) {
               Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
         },
         buf -> {
            int ingredientCount = buf.readVarInt();
            CompoundTag fluidTag = buf.readNbt();
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(fluidTag);
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

            for (int i = 0; i < ingredientCount; i++) {
               ingredients.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }

            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            return new ManaExtractorRecipe(output, ingredients, fluid);
         }
      );

      public MapCodec<ManaExtractorRecipe> codec() {
         return CODEC;
      }

      public StreamCodec<RegistryFriendlyByteBuf, ManaExtractorRecipe> streamCodec() {
         return STREAM_CODEC;
      }
   }
}
