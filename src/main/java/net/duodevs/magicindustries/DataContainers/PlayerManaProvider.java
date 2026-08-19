package net.duodevs.magicindustries.DataContainers;

import java.util.function.Supplier;
import net.duodevs.magicindustries.MagicIndustries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class PlayerManaProvider {
    private PlayerManaProvider() {}

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MagicIndustries.MOD_ID);

    public static final Supplier<AttachmentType<PlayerMana>> PLAYER_MANA = ATTACHMENT_TYPES.register(
            "player_mana",
            () -> AttachmentType.serializable(PlayerMana::new).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
