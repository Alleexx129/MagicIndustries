package net.duodevs.magicindustries.screen;

import java.util.function.Supplier;
import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {
    private ModMenuTypes() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MagicIndustries.MOD_ID);
    public static final Supplier<MenuType<ManaExtractorMenu>> MANA_EXTRACTOR_MENU =
            registerMenuType(ManaExtractorMenu::new, "mana_extractor_menu");
    public static final Supplier<MenuType<CoalGeneratorMenu>> COAL_GENERATOR_MENU =
            registerMenuType(CoalGeneratorMenu::new, "coal_generator_menu");

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
