package net.duodevs.magicindustries.fluid;

import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.item.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
   public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, "magicindustries");
   public static final RegistryObject<FlowingFluid> SOURCE_MANA_WATER = FLUIDS.register(
      "mana_water_fluid", () -> new Source(ModFluids.MANA_WATER_FLUID_PROPERTIES)
   );
   public static final RegistryObject<FlowingFluid> FLOWING_MANA_WATER = FLUIDS.register(
      "flowing_mana_water", () -> new Flowing(ModFluids.MANA_WATER_FLUID_PROPERTIES)
   );
   public static final Properties MANA_WATER_FLUID_PROPERTIES = new Properties(ModFluidTypes.MANA_WATER_FLUID_TYPE, SOURCE_MANA_WATER, FLOWING_MANA_WATER)
      .slopeFindDistance(2)
      .levelDecreasePerBlock(2)
      .block(ModBlocks.MANA_WATER_BLOCK)
      .bucket(ModItems.MANA_WATER_BUCKET);

   public static void register(IEventBus eventBus) {
      FLUIDS.register(eventBus);
   }
}
