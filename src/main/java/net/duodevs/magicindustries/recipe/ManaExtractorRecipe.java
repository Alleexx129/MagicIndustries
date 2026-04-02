package net.duodevs.magicindustries.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.duodevs.magicindustries.util.FluidJSONUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class ManaExtractorRecipe implements Recipe<SimpleContainer> {
   private final ResourceLocation id;
   private final ItemStack output;
   private final NonNullList<Ingredient> recipeItems;
   private final FluidStack fluidStack;

   public ManaExtractorRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, FluidStack fluidStack) {
      this.id = id;
      this.output = output;
      this.recipeItems = recipeItems;
      this.fluidStack = fluidStack;
   }

   public boolean matches(SimpleContainer pContainer, Level pLevel) {
      return pLevel.isClientSide() ? false : ((Ingredient)this.recipeItems.get(0)).test(pContainer.getItem(1));
   }

   public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
      return this.output;
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

   public ItemStack getResultItem(RegistryAccess registryAccess) {
      return this.output.copy();
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public RecipeSerializer<?> getSerializer() {
      return ManaExtractorRecipe.Serializer.INSTANCE;
   }

   public RecipeType<?> getType() {
      return ManaExtractorRecipe.Type.INSTANCE;
   }

   public static class Serializer implements RecipeSerializer<ManaExtractorRecipe> {
      public static final ManaExtractorRecipe.Serializer INSTANCE = new ManaExtractorRecipe.Serializer();
      public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("magicindustries", "gem_infusing");

      public ManaExtractorRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
         ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
         JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
         NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
         FluidStack fluid = FluidJSONUtil.readFluid(pSerializedRecipe.get("fluid").getAsJsonObject());

         for (int i = 0; i < inputs.size(); i++) {
            inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
         }

         return new ManaExtractorRecipe(pRecipeId, output, inputs, fluid);
      }

      @Nullable
      public ManaExtractorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
         NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
         FluidStack fluid = buf.readFluidStack();

         for (int i = 0; i < inputs.size(); i++) {
            inputs.set(i, Ingredient.fromNetwork(buf));
         }

         ItemStack output = buf.readItem();
         return new ManaExtractorRecipe(id, output, inputs, fluid);
      }

      public void toNetwork(FriendlyByteBuf buf, ManaExtractorRecipe recipe) {
         buf.writeInt(recipe.getIngredients().size());
         buf.writeFluidStack(recipe.fluidStack);

         for (Ingredient ing : recipe.getIngredients()) {
            ing.toNetwork(buf);
         }

         buf.writeItemStack(recipe.getResultItem(RegistryAccess.EMPTY), false);
      }
   }

   public static class Type implements RecipeType<ManaExtractorRecipe> {
      public static final ManaExtractorRecipe.Type INSTANCE = new ManaExtractorRecipe.Type();
      public static final String ID = "gem_infusing";

      private Type() {
      }
   }
}
