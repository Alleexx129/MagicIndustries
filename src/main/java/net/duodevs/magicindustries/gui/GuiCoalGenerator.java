package net.duodevs.magicindustries.gui;

import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;

import java.io.IOException;
import java.util.Collections;

public class GuiCoalGenerator extends GuiContainer {
    private static final ResourceLocation BG = new ResourceLocation(MagicIndustries.MODID, "textures/gui/coal_generator_gui.png");
    private static final ResourceLocation ARROW = new ResourceLocation(MagicIndustries.MODID, "textures/gui/arrow_fill.png");
    private final ContainerCoalGenerator menu;

    public GuiCoalGenerator(ContainerCoalGenerator menu) {
        super(menu);
        this.menu = menu;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // The 1.20.1 screen intentionally hides title/inventory labels.
    }

    @Override protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(BG);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int scaled = menu.tile.getMaxProgress() == 0 ? 0 : menu.tile.getProgress() * 81 / menu.tile.getMaxProgress();
        if (scaled > 0) {
            mc.getTextureManager().bindTexture(ARROW);
            drawModalRectWithCustomSizedTexture(guiLeft + 47, guiTop + 33, 0, 0, scaled, 20, 256, 256);
        }

        IEnergyStorage energy = menu.tile.getEnergyStorage();
        int h = energy.getMaxEnergyStored() == 0 ? 0 : energy.getEnergyStored() * 55 / energy.getMaxEnergyStored();
        if (h > 0) {
            drawGradientRect(guiLeft + 144, guiTop + 6 + 55 - h, guiLeft + 168, guiTop + 61, 0xFFB51500, 0xFF600B00);
        }
    }

    @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
        if (mouseX >= guiLeft + 144 && mouseX <= guiLeft + 168 && mouseY >= guiTop + 6 && mouseY <= guiTop + 61) {
            IEnergyStorage e = menu.tile.getEnergyStorage();
            drawHoveringText(Collections.singletonList(e.getEnergyStored() + " FE / " + e.getMaxEnergyStored() + " FE"), mouseX, mouseY);
        }
    }
}
