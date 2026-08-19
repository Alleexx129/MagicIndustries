package net.duodevs.magicindustries.item;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModArmorMaterial {
   public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, MagicIndustries.MOD_ID);
   public static final RegistryObject<ArmorMaterial> COPPER = ARMOR_MATERIALS.register("copper", () -> {
      Map<ArmorItem.Type, Integer> slotProtections = new EnumMap<>(ArmorItem.Type.class);
      slotProtections.put(ArmorItem.Type.BOOTS, 1);
      slotProtections.put(ArmorItem.Type.LEGGINGS, 3);
      slotProtections.put(ArmorItem.Type.CHESTPLATE, 4);
      slotProtections.put(ArmorItem.Type.HELMET, 2);
      slotProtections.put(ArmorItem.Type.BODY, 4);

      return new ArmorMaterial(
         slotProtections,
         12,
         SoundEvents.ARMOR_EQUIP_IRON,
         () -> Ingredient.of(ModItems.COPPER_INGOT.get()),
         List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "copper"))),
         0.0F,
         0.0F
      );
   });

   public static void register(IEventBus eventBus) {
      ARMOR_MATERIALS.register(eventBus);
   }
}
