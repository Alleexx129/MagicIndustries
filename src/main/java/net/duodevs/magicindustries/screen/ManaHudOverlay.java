package net.duodevs.magicindustries.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.duodevs.magicindustries.DataContainers.PlayerManaProvider;
import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.networking.packet.ClientManaData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ManaHudOverlay {
    private static final ResourceLocation MANA_BAR_TEXTURE_EMPTY = ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "textures/gui/mana_bar_empty.png");
    private static final ResourceLocation MANA_BAR_TEXTURE_FILL = ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "textures/gui/mana_bar_full.png");

    public static final IGuiOverlay HUD_MANA = (gui, guiGraphics, partialTick, width, height) -> {
        Player player = Minecraft.getInstance().player;
        if (player == null) {return;}

        int x = width / 2;
        int y = height;

        RenderSystem.setShaderTexture(1, MANA_BAR_TEXTURE_EMPTY);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        guiGraphics.blit(MANA_BAR_TEXTURE_EMPTY, x - 91, y - 58, 0, 0, 81, 8, 81, 11);

        RenderSystem.setShaderTexture(0, MANA_BAR_TEXTURE_FILL);

        int currentMana = ClientManaData.get();
        guiGraphics.blit(MANA_BAR_TEXTURE_FILL, x - 91, y - 58, 0, 0, (int) (81 * (currentMana / 100f)), 8, 81, 11);


        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    };
}