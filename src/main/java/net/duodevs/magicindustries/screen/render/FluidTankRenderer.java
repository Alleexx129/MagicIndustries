package net.duodevs.magicindustries.screen.render;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

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

   public void render(PoseStack poseStack, int x, int y, FluidStack fluidStack) {
      RenderSystem.enableBlend();
      poseStack.pushPose();
      poseStack.translate((float)x, (float)y, 0.0F);
      this.drawFluid(poseStack, this.width, this.height, fluidStack);
      poseStack.popPose();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
   }

   private void drawFluid(PoseStack poseStack, int width, int height, FluidStack fluidStack) {
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

         drawTiledSprite(poseStack, width, height, fluidColor, scaledAmount, fluidStillSprite);
      }
   }

   private TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
      Fluid fluid = fluidStack.getFluid();
      IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
      ResourceLocation fluidStill = renderProperties.getStillTexture(fluidStack);
      Minecraft minecraft = Minecraft.getInstance();
      return (TextureAtlasSprite)minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
   }

   private int getColorTint(FluidStack ingredient) {
      Fluid fluid = ingredient.getFluid();
      IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
      return renderProperties.getTintColor(ingredient);
   }

   private static void drawTiledSprite(PoseStack poseStack, int tiledWidth, int tiledHeight, int color, long scaledAmount, TextureAtlasSprite sprite) {
      RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
      Matrix4f matrix = poseStack.last().pose();
      setGLColorFromInt(color);
      int xTileCount = tiledWidth / 16;
      int xRemainder = tiledWidth - xTileCount * 16;
      long yTileCount = scaledAmount / 16L;
      long yRemainder = scaledAmount - yTileCount * 16L;
      int yStart = tiledHeight;

      for (int xTile = 0; xTile <= xTileCount; xTile++) {
         for (int yTile = 0; (long)yTile <= yTileCount; yTile++) {
            int width = xTile == xTileCount ? xRemainder : 16;
            long height = (long)yTile == yTileCount ? yRemainder : 16L;
            int x = xTile * 16;
            int y = yStart - (yTile + 1) * 16;
            if (width > 0 && height > 0L) {
               long maskTop = 16L - height;
               int maskRight = 16 - width;
               drawTextureWithMasking(matrix, (float)x, (float)y, sprite, maskTop, (long)maskRight, 100.0F);
            }
         }
      }
   }

   private static void setGLColorFromInt(int color) {
      float red = (float)(color >> 16 & 0xFF) / 255.0F;
      float green = (float)(color >> 8 & 0xFF) / 255.0F;
      float blue = (float)(color & 0xFF) / 255.0F;
      float alpha = (float)(color >> 24 & 0xFF) / 255.0F;
      RenderSystem.setShaderColor(red, green, blue, alpha);
   }

   private static void drawTextureWithMasking(
      Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, long maskRight, float zLevel
   ) {
      float uMin = textureSprite.getU0();
      float uMax = textureSprite.getU1();
      float vMin = textureSprite.getV0();
      float vMax = textureSprite.getV1();
      uMax -= (float)maskRight / 16.0F * (uMax - uMin);
      vMax -= (float)maskTop / 16.0F * (vMax - vMin);
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      Tesselator tessellator = Tesselator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuilder();
      bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      bufferBuilder.vertex(matrix, xCoord, yCoord + 16.0F, zLevel).uv(uMin, vMax).endVertex();
      bufferBuilder.vertex(matrix, xCoord + 16.0F - (float)maskRight, yCoord + 16.0F, zLevel).uv(uMax, vMax).endVertex();
      bufferBuilder.vertex(matrix, xCoord + 16.0F - (float)maskRight, yCoord + (float)maskTop, zLevel).uv(uMax, vMin).endVertex();
      bufferBuilder.vertex(matrix, xCoord, yCoord + (float)maskTop, zLevel).uv(uMin, vMin).endVertex();
      tessellator.end();
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
