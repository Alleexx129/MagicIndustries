package net.duodevs.magicindustries.screen;

import java.util.Optional;
import net.duodevs.magicindustries.screen.render.EnergyInfoArea2;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CoalGeneratorScreen extends AbstractContainerScreen<CoalGeneratorMenu> {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("magicindustries", "textures/gui/coal_generator_gui.png");
   private static final Identifier ARROW_TEXTURE = Identifier.fromNamespaceAndPath("magicindustries", "textures/gui/arrow_fill.png");
   private static final Identifier ENERGY_TEXTURE = Identifier.fromNamespaceAndPath("magicindustries", "textures/gui/energy_bar.png");
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
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
      this.energyInfoArea.draw(guiGraphics);
      this.renderProgressArrow(guiGraphics, x, y);
   }

   private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_TEXTURE, x + 47, y + 33, 0, 0, ((CoalGeneratorMenu)this.menu).getScaledProgress(), 20, 256, 256);
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      this.renderBackground(guiGraphics, mouseX, mouseY, delta);
      super.render(guiGraphics, mouseX, mouseY, delta);
      this.renderTooltip(guiGraphics, mouseX, mouseY);
   }

   protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
      super.renderTooltip(guiGraphics, mouseX, mouseY);
      if (this.isMouseOverEnergyArea(mouseX, mouseY)) {
         guiGraphics.setTooltipForNextFrame(this.font, this.energyInfoArea.getTooltips(), Optional.empty(), mouseX, mouseY);
      }
   }

   private boolean isMouseOverEnergyArea(int mouseX, int mouseY) {
      return mouseX >= (this.width - this.imageWidth) / 2 + 144
         && mouseX <= (this.width - this.imageWidth) / 2 + 168
         && mouseY >= (this.height - this.imageHeight) / 2 + 6
         && mouseY <= (this.height - this.imageHeight) / 2 + 61;
   }
}
