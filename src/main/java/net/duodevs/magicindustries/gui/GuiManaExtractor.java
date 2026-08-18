package net.duodevs.magicindustries.gui;

import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class GuiManaExtractor extends GuiContainer {
    private static final ResourceLocation BG = new ResourceLocation(MagicIndustries.MODID, "textures/gui/mana_extractor_gui.png");
    private static final ResourceLocation ARROW = new ResourceLocation(MagicIndustries.MODID, "textures/gui/mana_extractor_arrow.png");
    private final ContainerManaExtractor menu;

    public GuiManaExtractor(ContainerManaExtractor menu) {
        super(menu);
        this.menu = menu;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

    @Override protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(BG);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int scaled = menu.tile.getMaxProgress() == 0 ? 0 : menu.tile.getProgress() * 56 / menu.tile.getMaxProgress();
        if (scaled > 0) {
            mc.getTextureManager().bindTexture(ARROW);
            drawModalRectWithCustomSizedTexture(guiLeft + 45, guiTop + 30, 0, 0, scaled, 20, 256, 256);
        }

        drawFluid(guiLeft + 155, guiTop + 11, 14, 58, menu.tile.getWaterTank().getFluidAmount(), 64000,
                FluidRegistry.WATER.getStill(), FluidRegistry.WATER.getColor());
        drawFluid(guiLeft + 104, guiTop + 11, 14, 58, menu.tile.getManaTank().getFluidAmount(), 64000,
                ModFluids.MANA_WATER.getStill(), ModFluids.MANA_WATER.getColor());
    }

    private void drawFluid(int x, int y, int width, int height, int amount, int capacity, ResourceLocation still, int color) {
        if (amount <= 0 || capacity <= 0 || still == null) return;
        int filled = Math.max(1, amount * height / capacity);
        TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(still.toString());
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        float a = ((color >>> 24) & 0xFF) / 255F;
        if (a <= 0F) a = 1F;
        float r = ((color >>> 16) & 0xFF) / 255F;
        float g = ((color >>> 8) & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        GlStateManager.color(r, g, b, a);
        drawTexturedModalRect(x, y + height - filled, sprite, width, filled);
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);

        if (inside(mouseX, mouseY, guiLeft + 156, guiTop + 12, 14, 58)) {
            drawHoveringText(Arrays.asList(menu.tile.getWaterTank().getFluidAmount() + " mB / " + menu.tile.getWaterTank().getCapacity() + " mB", "Water"), mouseX, mouseY);
        } else if (inside(mouseX, mouseY, guiLeft + 105, guiTop + 12, 14, 58)) {
            drawHoveringText(Arrays.asList(menu.tile.getManaTank().getFluidAmount() + " mB / " + menu.tile.getManaTank().getCapacity() + " mB", "Liquid Mana"), mouseX, mouseY);
        } else if (inside(mouseX, mouseY, guiLeft + 28, guiTop + 4, 12, 18)) {
            String text = I18n.translateToLocal("gui.magicindustries.manaextractorhelp");
            List<String> lines = new ArrayList<>();
            for (String line : text.split("\\n")) lines.add(line);
            drawHoveringText(lines, mouseX, mouseY);
        }
    }

    private static boolean inside(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }
}
