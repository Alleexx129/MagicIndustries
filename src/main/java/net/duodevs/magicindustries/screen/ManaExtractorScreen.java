package net.duodevs.magicindustries.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import net.duodevs.magicindustries.screen.render.FluidTankRenderer;
import net.duodevs.magicindustries.screen.render.HelpInfoArea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag.Default;

public class ManaExtractorScreen extends AbstractContainerScreen<ManaExtractorMenu> {
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("magicindustries", "textures/gui/mana_extractor_gui.png");
   private HelpInfoArea helpInfoArea;
   private FluidTankRenderer renderer;
   private static final ResourceLocation ARROW_TEXTURE = ResourceLocation.fromNamespaceAndPath("magicindustries", "textures/gui/mana_extractor_arrow.png");

   public ManaExtractorScreen(ManaExtractorMenu menu, Inventory inventory, Component component) {
      super(menu, inventory, component);
   }

   protected void init() {
      super.init();
      this.assignHelpInfoArea();
      this.assignFluidRenderer();
      this.inventoryLabelY = 10000;
      this.titleLabelY = 10000;
   }

   protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, TEXTURE);
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
      this.renderProgressArrow(guiGraphics, x, y);
      this.renderer.render(guiGraphics.pose(), x + 155, y + 11, ((ManaExtractorMenu)this.menu).getFluidStack());
      this.renderer.render(guiGraphics.pose(), x + 104, y + 11, ((ManaExtractorMenu)this.menu).getFluidStackMana());
   }

   private void assignFluidRenderer() {
      this.renderer = new FluidTankRenderer(64000L, true, 14, 58);
   }

   private void assignHelpInfoArea() {
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      this.helpInfoArea = new HelpInfoArea(x + 28, y + 4, Component.translatable("gui.magicindustries.manaextractorhelp"));
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      this.renderBackground(guiGraphics);
      super.render(guiGraphics, mouseX, mouseY, delta);
      this.renderTooltip(guiGraphics, mouseX, mouseY);
   }

   protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
      super.renderTooltip(guiGraphics, mouseX, mouseY);
      if (this.isMouseOverHelpArea(mouseX, mouseY)) {
         guiGraphics.renderTooltip(this.font, this.helpInfoArea.getTooltips(), Optional.empty(), mouseX, mouseY);
      }

      if (this.isMouseOverFluidTank(mouseX, mouseY)) {
         guiGraphics.renderTooltip(
            this.font, this.renderer.getTooltip(((ManaExtractorMenu)this.menu).getFluidStack(), Default.NORMAL, "Water"), Optional.empty(), mouseX, mouseY
         );
      }

      if (this.isMouseOverFluidTankMana(mouseX, mouseY)) {
         guiGraphics.renderTooltip(
            this.font,
            this.renderer.getTooltip(((ManaExtractorMenu)this.menu).getFluidStackMana(), Default.NORMAL, "Liquid Mana"),
            Optional.empty(),
            mouseX,
            mouseY
         );
      }
   }

   private boolean isMouseOverHelpArea(int mouseX, int mouseY) {
      return mouseX >= (this.width - this.imageWidth) / 2 + 28
         && mouseX <= (this.width - this.imageWidth) / 2 + 40
         && mouseY >= (this.height - this.imageHeight) / 2 + 4
         && mouseY <= (this.height - this.imageHeight) / 2 + 22;
   }

   private boolean isMouseOverFluidTank(int mouseX, int mouseY) {
      return mouseX >= (this.width - this.imageWidth) / 2 + 156
         && mouseX <= (this.width - this.imageWidth) / 2 + 169
         && mouseY >= (this.height - this.imageHeight) / 2 + 12
         && mouseY <= (this.height - this.imageHeight) / 2 + 69;
   }

   private boolean isMouseOverFluidTankMana(int mouseX, int mouseY) {
      return mouseX >= (this.width - this.imageWidth) / 2 + 105
         && mouseX <= (this.width - this.imageWidth) / 2 + 118
         && mouseY >= (this.height - this.imageHeight) / 2 + 12
         && mouseY <= (this.height - this.imageHeight) / 2 + 69;
   }

   private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
      guiGraphics.blit(ARROW_TEXTURE, x + 45, y + 30, 0, 0, ((ManaExtractorMenu)this.menu).getScaledProgress(), 20);
   }
}
