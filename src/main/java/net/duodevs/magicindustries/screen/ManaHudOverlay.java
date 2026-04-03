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
        if (player == null) return;
        int currentMana = ClientManaData.get();

        if (currentMana > 0) {

            int x = width / 2;
            int y = height;

            int armorValue = player.getArmorValue();

            int yOffset = -48;

            if (armorValue > 0) {
                yOffset -= 10;
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            guiGraphics.blit(MANA_BAR_TEXTURE_EMPTY, x - 91, y + yOffset, 0, 0, 81, 8, 81, 11);



            int fillWidth = (int) (81 * (currentMana / 100f));
            guiGraphics.blit(MANA_BAR_TEXTURE_FILL, x - 91, y + yOffset, 0, 0, fillWidth, 8, 81, 11);
        }

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    };
}