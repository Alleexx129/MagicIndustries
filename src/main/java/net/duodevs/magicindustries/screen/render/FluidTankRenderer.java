package net.duodevs.magicindustries.screen.render;

import com.google.common.base.Preconditions;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FluidTankRenderer {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final NumberFormat nf = NumberFormat.getIntegerInstance();
   private static final int TEXTURE_SIZE = 16;
   private static final int MIN_FLUID_HEIGHT = 1;
   private final long capacity;
   private final FluidTankRenderer.TooltipMode tooltipMode;
   private final int width;
   private final int height;

   public FluidTankRenderer(long capacity, boolean showCapacity, int width, int height) {
      this(capacity, showCapacity ? FluidTankRenderer.TooltipMode.SHOW_AMOUNT_AND_CAPACITY : FluidTankRenderer.TooltipMode.SHOW_AMOUNT, width, height);
   }

   private FluidTankRenderer(long capacity, FluidTankRenderer.TooltipMode tooltipMode, int width, int height) {
      Preconditions.checkArgument(capacity > 0L, "capacity must be > 0");
      Preconditions.checkArgument(width > 0, "width must be > 0");
      Preconditions.checkArgument(height >= 0, "height must be > 0");
      this.capacity = capacity;
      this.tooltipMode = tooltipMode;
      this.width = width;
      this.height = height;
   }

   public void render(GuiGraphics guiGraphics, int x, int y, FluidStack fluidStack) {
      this.drawFluid(guiGraphics, x, y, this.width, this.height, fluidStack);
   }

   private void drawFluid(GuiGraphics guiGraphics, int x, int y, int width, int height, FluidStack fluidStack) {
      Fluid fluid = fluidStack.getFluid();
      if (!fluid.isSame(Fluids.EMPTY)) {
         TextureAtlasSprite fluidStillSprite = this.getStillFluidSprite(fluidStack);
         int fluidColor = this.getColorTint(fluidStack);
         long amount = (long)fluidStack.getAmount();
         long scaledAmount = amount * (long)height / this.capacity;
         if (amount > 0L && scaledAmount < 1L) {
            scaledAmount = 1L;
         }

         if (scaledAmount > (long)height) {
            scaledAmount = (long)height;
         }

         if (scaledAmount > 0L) {
            int fluidHeight = (int)scaledAmount;
            int fluidY = y + height - fluidHeight;
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, fluidStillSprite, x, fluidY, width, fluidHeight, fluidColor);
         }
      }
   }

   private TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
      Fluid fluid = fluidStack.getFluid();
      IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
      Identifier fluidStill = renderProperties.getStillTexture(fluidStack);

      return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(fluidStill);
   }

   private int getColorTint(FluidStack ingredient) {
      Fluid fluid = ingredient.getFluid();
      IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
      return renderProperties.getTintColor(ingredient);
   }

   public List<Component> getTooltip(FluidStack fluidStack, TooltipFlag tooltipFlag) {
      List<Component> tooltip = new ArrayList<>();
      Fluid fluidType = fluidStack.getFluid();

      try {
         if (fluidType.isSame(Fluids.EMPTY)) {
            return tooltip;
         }

         Component displayName = fluidStack.getDisplayName();
         tooltip.add(displayName);
         long amount = (long)fluidStack.getAmount();
         long milliBuckets = amount * 1000L / 1000L;
         if (this.tooltipMode == FluidTankRenderer.TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
            MutableComponent amountString = Component.translatable(
               "magicindustries.tooltip.liquid.amount.with.capacity", new Object[]{nf.format(milliBuckets), nf.format(this.capacity)}
            );
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
         } else if (this.tooltipMode == FluidTankRenderer.TooltipMode.SHOW_AMOUNT) {
            MutableComponent amountString = Component.translatable("magicindustries.tooltip.liquid.amount", new Object[]{nf.format(milliBuckets)});
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
         }
      } catch (RuntimeException var11) {
         LOGGER.error("Failed to get tooltip for fluid: " + var11);
      }

      return tooltip;
   }

   public List<Component> getTooltip(FluidStack fluidStack, TooltipFlag tooltipFlag, String fluidName) {
      List<Component> tooltip = new ArrayList<>();

      try {
         Component displayName = Component.literal(fluidName);
         tooltip.add(displayName);
         long amount = (long)fluidStack.getAmount();
         long milliBuckets = amount * 1000L / 1000L;
         if (this.tooltipMode == FluidTankRenderer.TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
            MutableComponent amountString = Component.translatable(
               "magicindustries.tooltip.liquid.amount.with.capacity", new Object[]{nf.format(milliBuckets), nf.format(this.capacity)}
            );
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
         } else if (this.tooltipMode == FluidTankRenderer.TooltipMode.SHOW_AMOUNT) {
            MutableComponent amountString = Component.translatable("magicindustries.tooltip.liquid.amount", new Object[]{nf.format(milliBuckets)});
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
         }
      } catch (RuntimeException var11) {
         LOGGER.error("Failed to get tooltip for fluid: " + var11);
      }

      return tooltip;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   static enum TooltipMode {
      SHOW_AMOUNT,
      SHOW_AMOUNT_AND_CAPACITY,
      ITEM_LIST;
   }
}
