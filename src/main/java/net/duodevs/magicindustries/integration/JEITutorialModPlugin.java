package net.duodevs.magicindustries.integration;

import java.util.List;
import java.util.Objects;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.duodevs.magicindustries.recipe.ManaExtractorRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class JEITutorialModPlugin implements IModPlugin {
   public static RecipeType<ManaExtractorRecipe> INFUSION_TYPE = new RecipeType(ManaExtractorRecipeCategory.UID, ManaExtractorRecipe.class);

   public ResourceLocation getPluginUid() {
      return ResourceLocation.fromNamespaceAndPath("magicindustries", "jei_plugin");
   }

   public void registerCategories(IRecipeCategoryRegistration registration) {
      registration.addRecipeCategories(new IRecipeCategory[]{new ManaExtractorRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
   }

   public void registerRecipes(IRecipeRegistration registration) {
      RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
      List<ManaExtractorRecipe> recipesInfusing = rm.getAllRecipesFor(ManaExtractorRecipe.Type.INSTANCE);
      registration.addRecipes(INFUSION_TYPE, recipesInfusing);
   }
}
