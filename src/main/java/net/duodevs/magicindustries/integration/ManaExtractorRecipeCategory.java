package net.duodevs.magicindustries.integration;

import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.recipe.ManaExtractorRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ManaExtractorRecipeCategory implements IRecipeCategory<ManaExtractorRecipe> {
   public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("magicindustries", "gem_infusing");
   public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("magicindustries", "textures/gui/mana_extractor_gui.png");
   private final IDrawable background;
   private final IDrawable icon;

   public ManaExtractorRecipeCategory(IGuiHelper helper) {
      this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
      this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack((ItemLike)ModBlocks.MANA_EXTRACTOR.get()));
   }

   public RecipeType<ManaExtractorRecipe> getRecipeType() {
      return JEITutorialModPlugin.INFUSION_TYPE;
   }

   public Component getTitle() {
      return Component.literal("Mana Extractor");
   }

   public IDrawable getBackground() {
      return this.background;
   }

   public IDrawable getIcon() {
      return this.icon;
   }

   public void setRecipe(IRecipeLayoutBuilder builder, ManaExtractorRecipe recipe, IFocusGroup focuses) {
      builder.addSlot(RecipeIngredientRole.INPUT, 86, 15).addIngredients((Ingredient)recipe.getIngredients().get(0));
      ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 55, 15).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid())))
         .setFluidRenderer(64000L, false, 16, 61);
      builder.addSlot(RecipeIngredientRole.OUTPUT, 86, 60).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
   }
}
