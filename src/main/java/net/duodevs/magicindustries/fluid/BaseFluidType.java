package net.duodevs.magicindustries.fluid;

import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidType.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BaseFluidType extends FluidType {
   private final Identifier stillTexture;
   private final Identifier flowingTexture;
   private final Identifier overlayTexture;
   private final int tintColor;
   private final Vector3f fogColor;

   public BaseFluidType(
      Identifier stillTexture, Identifier flowingTexture, Identifier overlayTexture, int tintColor, Vector3f fogColor, Properties properties
   ) {
      super(properties);
      this.stillTexture = stillTexture;
      this.flowingTexture = flowingTexture;
      this.overlayTexture = overlayTexture;
      this.tintColor = tintColor;
      this.fogColor = fogColor;
   }

   public Identifier getStillTexture() {
      return this.stillTexture;
   }

   public Identifier getFlowingTexture() {
      return this.flowingTexture;
   }

   public int getTintColor() {
      return this.tintColor;
   }

   public Identifier getOverlayTexture() {
      return this.overlayTexture;
   }

   public Vector3f getFogColor() {
      return this.fogColor;
   }

   public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
      consumer.accept(
         new IClientFluidTypeExtensions() {
            public Identifier getStillTexture() {
               return BaseFluidType.this.stillTexture;
            }

            public Identifier getFlowingTexture() {
               return BaseFluidType.this.flowingTexture;
            }

            @Nullable
            public Identifier getOverlayTexture() {
               return BaseFluidType.this.overlayTexture;
            }

            public int getTintColor() {
               return BaseFluidType.this.tintColor;
            }

            @NotNull
            public Vector3f modifyFogColor(
               Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor
            ) {
               return BaseFluidType.this.fogColor;
            }

         }
      );
   }
}
