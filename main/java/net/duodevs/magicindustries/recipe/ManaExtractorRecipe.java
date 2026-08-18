package net.duodevs.magicindustries.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * 1.12 representation of the original gem_infusing recipe type.
 *
 * The 1.20.1 mod ships the serializer/type and JEI category even though version 1.5.2
 * contains no built-in gem_infusing data recipe. This class keeps that extension point
 * usable by resource packs/modpacks on 1.12.2.
 */
public class ManaExtractorRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final FluidStack fluidStack;

    public ManaExtractorRecipe(ItemStack output, NonNullList<Ingredient> recipeItems, FluidStack fluidStack) {
        this.output = output.copy();
        this.recipeItems = recipeItems;
        this.fluidStack = fluidStack == null ? null : fluidStack.copy();
    }

    /** Matches the original recipe's item check: ingredient 0 is tested against machine inventory slot 1. */
    public boolean matchesMachine(ItemStack slot1, World world) {
        return world != null && !world.isRemote && !recipeItems.isEmpty() && recipeItems.get(0).apply(slot1);
    }

    public FluidStack getFluid() { return fluidStack == null ? null : fluidStack.copy(); }
    public Ingredient getMachineIngredient() { return recipeItems.isEmpty() ? Ingredient.EMPTY : recipeItems.get(0); }
    public ItemStack getMachineOutput() { return output.copy(); }

    /*
     * This custom machine recipe is intentionally not craftable in a crafting grid.
     * It still implements IRecipe because Forge 1.12 JSON recipe factories register through that registry.
     */
    @Override public boolean matches(InventoryCrafting inv, World worldIn) { return false; }
    @Override public ItemStack getCraftingResult(InventoryCrafting inv) { return output.copy(); }
    @Override public boolean canFit(int width, int height) { return true; }
    @Override public ItemStack getRecipeOutput() { return output.copy(); }
    @Override public NonNullList<Ingredient> getIngredients() { return recipeItems; }
    @Override public boolean isDynamic() { return true; }

    public static final class Factory implements IRecipeFactory {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            JsonArray ingredients = JsonUtils.getJsonArray(json, "ingredients");
            if (ingredients.size() < 1) throw new JsonParseException("gem_infusing requires at least one ingredient");

            NonNullList<Ingredient> list = NonNullList.withSize(1, Ingredient.EMPTY);
            list.set(0, CraftingHelper.getIngredient(ingredients.get(0), context));
            ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "output"), context);
            FluidStack fluid = readFluid(JsonUtils.getJsonObject(json, "fluid"));
            return new ManaExtractorRecipe(output, list, fluid);
        }

        /**
         * Forge 1.20 serializes FluidStack through a codec. To keep authored recipes portable,
         * accept the common codec spellings plus the concise 1.12-friendly form.
         */
        private static FluidStack readFluid(JsonObject json) {
            String name = firstString(json, "fluid", "FluidName", "id", "name");
            if (name == null || name.isEmpty()) throw new JsonParseException("fluid object is missing a fluid id");
            Fluid fluid = FluidRegistry.getFluid(name);
            if (fluid == null && name.indexOf(':') >= 0) fluid = FluidRegistry.getFluid(name.substring(name.indexOf(':') + 1));
            if (fluid == null) throw new JsonParseException("Unknown fluid '" + name + "'");
            int amount = json.has("amount") ? JsonUtils.getInt(json, "amount") : JsonUtils.getInt(json, "Amount", 1000);
            if (amount <= 0) throw new JsonParseException("Fluid amount must be positive");
            return new FluidStack(fluid, amount);
        }

        private static String firstString(JsonObject json, String... keys) {
            for (String key : keys) if (json.has(key) && json.get(key).isJsonPrimitive()) return json.get(key).getAsString();
            return null;
        }
    }
}
