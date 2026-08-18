package net.duodevs.magicindustries.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public final class ModFluids {
    public static Fluid MANA_WATER;
    private ModFluids() {}

    /**
     * Registers the 1.12 representation of the source mod's mana-water SourceFluid.
     * The 1.20 fluid deliberately reuses vanilla water sprites and applies a cyan tint.
     */
    public static synchronized Fluid ensureRegistered() {
        if (MANA_WATER != null) return MANA_WATER;
        Fluid existing = FluidRegistry.getFluid("mana_water_fluid");
        if (existing != null) {
            MANA_WATER = existing;
            return existing;
        }

        Fluid candidate = new Fluid(
            "mana_water_fluid",
            new ResourceLocation("minecraft", "blocks/water_still"),
            new ResourceLocation("minecraft", "blocks/water_flow")
        ).setLuminosity(2).setDensity(15).setViscosity(5).setColor(0xFF00E7FF);

        FluidRegistry.registerFluid(candidate);
        MANA_WATER = FluidRegistry.getFluid("mana_water_fluid");
        if (MANA_WATER == null) MANA_WATER = candidate;
        return MANA_WATER;
    }

    public static void preInit() {
        ensureRegistered();
    }
}
