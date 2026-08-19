package net.duodevs.magicindustries.fluid;

import java.util.function.Supplier;
import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidType.Properties;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

public final class ModFluidTypes {
    private ModFluidTypes() {}

    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.withDefaultNamespace("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.withDefaultNamespace("block/water_flow");
    public static final ResourceLocation MANA_OVERLAY_RL = ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "misc/in_mana_water");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, MagicIndustries.MOD_ID);

    public static final Supplier<FluidType> MANA_WATER_FLUID_TYPE = register(
            "mana_water_fluid",
            Properties.create().lightLevel(2).density(15).viscosity(5)
                    .sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK)
    );

    private static Supplier<FluidType> register(String name, Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(
                WATER_STILL_RL,
                WATER_FLOWING_RL,
                MANA_OVERLAY_RL,
                -16717825,
                new Vector3f(0.8784314F, 0.21960784F, 0.8156863F),
                properties
        ));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
