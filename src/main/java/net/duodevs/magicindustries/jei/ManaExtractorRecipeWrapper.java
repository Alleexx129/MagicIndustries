package net.duodevs.magicindustries.jei;

import java.util.Arrays;
import java.util.Collections;
// import mezz.jei.api.ingredients.IIngredients;
// import mezz.jei.api.recipe.IRecipeWrapper;
import net.duodevs.magicindustries.recipe.ManaExtractorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
/*
public final class ManaExtractorRecipeWrapper implements IRecipeWrapper {
    private final ManaExtractorRecipe recipe;

    public ManaExtractorRecipeWrapper(ManaExtractorRecipe recipe) { this.recipe = recipe; }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class,
            Collections.singletonList(Arrays.asList(recipe.getMachineIngredient().getMatchingStacks())));
        FluidStack fluid = recipe.getFluid();
        if (fluid != null) ingredients.setInput(FluidStack.class, fluid);
        ingredients.setOutput(ItemStack.class, recipe.getMachineOutput());
    }
}*/
