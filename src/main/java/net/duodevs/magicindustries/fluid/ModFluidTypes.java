package net.duodevs.magicindustries.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidType.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import org.joml.Vector3f;

public class ModFluidTypes {
   public static final ResourceLocation WATER_STILL_RL = ResourceLocation.withDefaultNamespace("block/water_still");
   public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.withDefaultNamespace("block/water_flow");
   public static final ResourceLocation MANA_OVERLAY_RL = ResourceLocation.fromNamespaceAndPath("magicindustries", "misc/in_mana_water");
   public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(Keys.FLUID_TYPES, "magicindustries");
   public static final RegistryObject<FluidType> MANA_WATER_FLUID_TYPE = register(
      "mana_water_fluid", Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK)
   );

   private static RegistryObject<FluidType> register(String name, Properties properties) {
      return FLUID_TYPES.register(
         name,
         () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, MANA_OVERLAY_RL, -16717825, new Vector3f(0.8784314F, 0.21960784F, 0.8156863F), properties)
      );
   }

   public static void register(IEventBus eventBus) {
      FLUID_TYPES.register(eventBus);
   }
}
