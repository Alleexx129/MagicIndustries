package net.duodevs.magicindustries.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import net.duodevs.magicindustries.screen.render.EnergyInfoArea2;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CoalGeneratorScreen extends AbstractContainerScreen<CoalGeneratorMenu> {
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("magicindustries", "textures/gui/coal_generator_gui.png");
   private static final ResourceLocation ARROW_TEXTURE = ResourceLocation.fromNamespaceAndPath("magicindustries", "textures/gui/arrow_fill.png");
   private static final ResourceLocation ENERGY_TEXTURE = ResourceLocation.fromNamespaceAndPath("magicindustries", "textures/gui/energy_bar.png");
   private EnergyInfoArea2 energyInfoArea;

   public CoalGeneratorScreen(CoalGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
      super(pMenu, pPlayerInventory, pTitle);
   }

   protected void init() {
      super.init();
      this.inventoryLabelY = 10000;
      this.titleLabelY = 10000;
      this.assignEnergyInfoArea();
   }

   private void assignEnergyInfoArea() {
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      this.energyInfoArea = new EnergyInfoArea2(x + 144, y + 6, ((CoalGeneratorMenu)this.menu).blockEntity.getEnergyStorage());
   }

   protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, TEXTURE);
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
      this.energyInfoArea.draw(guiGraphics);
      this.renderProgressArrow(guiGraphics, x, y);
      this.renderTooltip(guiGraphics, pMouseX, pMouseY);
   }

   private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
      guiGraphics.blit(ARROW_TEXTURE, x + 47, y + 33, 0, 0, ((CoalGeneratorMenu)this.menu).getScaledProgress(), 20);
      this.energyInfoArea.draw(guiGraphics);
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      this.renderBackground(guiGraphics);
      super.render(guiGraphics, mouseX, mouseY, delta);
      this.renderTooltip(guiGraphics, mouseX, mouseY);
   }

   protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
      super.renderTooltip(guiGraphics, mouseX, mouseY);
      if (this.isMouseOverEnergyArea(mouseX, mouseY)) {
         guiGraphics.renderTooltip(this.font, this.energyInfoArea.getTooltips(), Optional.empty(), mouseX, mouseY);
      }
   }

   private boolean isMouseOverEnergyArea(int mouseX, int mouseY) {
      return mouseX >= (this.width - this.imageWidth) / 2 + 144
         && mouseX <= (this.width - this.imageWidth) / 2 + 168
         && mouseY >= (this.height - this.imageHeight) / 2 + 6
         && mouseY <= (this.height - this.imageHeight) / 2 + 61;
   }
}
