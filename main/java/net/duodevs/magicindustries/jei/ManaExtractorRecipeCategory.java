package net.duodevs.magicindustries.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.init.ModContent;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class ManaExtractorRecipeCategory implements IRecipeCategory<ManaExtractorRecipeWrapper> {
    public static final String UID = MagicIndustries.MODID + ":gem_infusing";
    private static final ResourceLocation TEXTURE = new ResourceLocation(MagicIndustries.MODID, "textures/gui/mana_extractor_gui.png");
    private final IDrawable background;
    private final IDrawable icon;

    public ManaExtractorRecipeCategory(IGuiHelper guiHelper) {
        // Original 1.20 category uses the first 176x85 pixels of the extractor GUI.
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModContent.MANA_EXTRACTOR));
    }

    @Override public String getUid() { return UID; }
    @Override public String getTitle() { return I18n.format("magicindustries.jei.mana_extractor"); }
    @Override public String getModName() { return MagicIndustries.NAME; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ManaExtractorRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        items.init(0, true, 86, 15);
        items.init(1, false, 86, 60);
        items.set(ingredients);

        IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
        fluids.init(0, true, 55, 15, 16, 61, 64000, false, null);
        fluids.set(ingredients);
    }
}
