package net.duodevs.magicindustries.jei;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.duodevs.magicindustries.gui.GuiManaExtractor;
import net.duodevs.magicindustries.init.ModContent;
import net.duodevs.magicindustries.recipe.ManaExtractorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@JEIPlugin
public final class MagicIndustriesJeiPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new ManaExtractorRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(ManaExtractorRecipe.class, ManaExtractorRecipeWrapper::new, ManaExtractorRecipeCategory.UID);
        List<ManaExtractorRecipe> recipes = new ArrayList<>();
        for (IRecipe recipe : ForgeRegistries.RECIPES.getValuesCollection()) {
            if (recipe instanceof ManaExtractorRecipe) recipes.add((ManaExtractorRecipe) recipe);
        }
        if (!recipes.isEmpty()) registry.addRecipes(recipes, ManaExtractorRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModContent.MANA_EXTRACTOR), ManaExtractorRecipeCategory.UID);
        registry.addRecipeClickArea(GuiManaExtractor.class, 45, 30, 56, 20, ManaExtractorRecipeCategory.UID);
    }
}
