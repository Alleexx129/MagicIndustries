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
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
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

   @Override
   public boolean matches(SingleRecipeInput input, Level level) {
      return !level.isClientSide()
              && !this.recipeItems.isEmpty()
              && this.recipeItems.getFirst().test(input.getItem(0));
   }

   @Override
   public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
      return this.output.copy();
   }

   public FluidStack getFluid() {
      return this.fluidStack;
   }

   public ItemStack getResultItem(HolderLookup.Provider registries) {
      return this.output.copy();
   }

   @Override
   public RecipeSerializer<ManaExtractorRecipe> getSerializer() {
      return ModRecipes.MANA_EXTRACTOR_SERIALIZER.get();
   }

   @Override
   public RecipeType<ManaExtractorRecipe> getType() {
      return ModRecipes.MANA_EXTRACTOR_TYPE.get();
   }

   @Override
   public PlacementInfo placementInfo() {
      return PlacementInfo.create(this.recipeItems);
   }

   @Override
   public RecipeBookCategory recipeBookCategory() {
      return RecipeBookCategories.CRAFTING_MISC;
   }

   public static class Serializer implements RecipeSerializer<ManaExtractorRecipe> {
      public static final Serializer INSTANCE = new Serializer();

      public static final MapCodec<ManaExtractorRecipe> CODEC =
              RecordCodecBuilder.mapCodec(instance -> instance.group(
                      ItemStack.CODEC
                              .fieldOf("output")
                              .forGetter(recipe -> recipe.output),

                      Ingredient.CODEC
                              .listOf()
                              .fieldOf("ingredients")
                              .forGetter(recipe -> recipe.recipeItems),

                      FluidStack.CODEC
                              .fieldOf("fluid")
                              .forGetter(ManaExtractorRecipe::getFluid)
              ).apply(instance, ManaExtractorRecipe::new));

      public static final StreamCodec<RegistryFriendlyByteBuf, ManaExtractorRecipe> STREAM_CODEC =
              StreamCodec.of(
                      (buf, recipe) -> {
                         buf.writeVarInt(recipe.recipeItems.size());

                         buf.writeNbt(
                                 recipe.fluidStack.writeToNBT(new CompoundTag())
                         );

                         for (Ingredient ingredient : recipe.recipeItems) {
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                         }

                         ItemStack.STREAM_CODEC.encode(buf, recipe.output);
                      },

                      buf -> {
                         int ingredientCount = buf.readVarInt();

                         CompoundTag fluidTag = buf.readNbt();
                         FluidStack fluid = FluidStack.loadFluidStackFromNBT(fluidTag);

                         NonNullList<Ingredient> ingredients = NonNullList.create();

                         for (int i = 0; i < ingredientCount; i++) {
                            ingredients.add(
                                    Ingredient.CONTENTS_STREAM_CODEC.decode(buf)
                            );
                         }

                         ItemStack output = ItemStack.STREAM_CODEC.decode(buf);

                         return new ManaExtractorRecipe(
                                 output,
                                 ingredients,
                                 fluid
                         );
                      }
              );

      @Override
      public MapCodec<ManaExtractorRecipe> codec() {
         return CODEC;
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, ManaExtractorRecipe> streamCodec() {
         return STREAM_CODEC;
      }
   }
}