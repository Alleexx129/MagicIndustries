package net.duodevs.magicindustries.screen;

import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.networking.packet.ClientManaData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraftforge.client.gui.overlay.ForgeLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class ManaHudOverlay {
    private static final Identifier MANA_BAR_TEXTURE_EMPTY = Identifier.fromNamespaceAndPath(MagicIndustries.MOD_ID, "textures/gui/mana_bar_empty.png");
    private static final Identifier MANA_BAR_TEXTURE_FILL = Identifier.fromNamespaceAndPath(MagicIndustries.MOD_ID, "textures/gui/mana_bar_full.png");

    public static final ForgeLayer HUD_MANA = (guiGraphics, deltaTracker) -> {
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
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

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, MANA_BAR_TEXTURE_EMPTY, x - 91, y + yOffset, 0, 0, 81, 8, 90, 9);

            int fillWidth = (int) (81 * (currentMana / 100f));
            if (fillWidth > 0) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, MANA_BAR_TEXTURE_FILL, x - 91, y + yOffset, 0, 0, fillWidth, 8, 90, 9);
            }
        }
    };
}