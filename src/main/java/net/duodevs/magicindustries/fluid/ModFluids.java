package net.duodevs.magicindustries.fluid;

import java.util.function.Supplier;
import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModFluids {
    private ModFluids() {}

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, MagicIndustries.MOD_ID);

    public static final Supplier<FlowingFluid> SOURCE_MANA_WATER = FLUIDS.register(
            "mana_water_fluid", () -> new BaseFlowingFluid.Source(ModFluids.MANA_WATER_FLUID_PROPERTIES)
    );
    public static final Supplier<FlowingFluid> FLOWING_MANA_WATER = FLUIDS.register(
            "flowing_mana_water", () -> new BaseFlowingFluid.Flowing(ModFluids.MANA_WATER_FLUID_PROPERTIES)
    );

    public static final BaseFlowingFluid.Properties MANA_WATER_FLUID_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.MANA_WATER_FLUID_TYPE, SOURCE_MANA_WATER, FLOWING_MANA_WATER)
                    .slopeFindDistance(2)
                    .levelDecreasePerBlock(2)
                    .block(ModBlocks.MANA_WATER_BLOCK)
                    .bucket(ModItems.MANA_WATER_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
