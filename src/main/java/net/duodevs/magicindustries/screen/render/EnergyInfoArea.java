package net.duodevs.magicindustries.screen.render;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyInfoArea extends InfoArea {
   private final IEnergyStorage energy;

   public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy) {
      this(xMin, yMin, energy, 8, 64);
   }

   public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy, int width, int height) {
      super(new Rect2i(xMin, yMin, width, height));
      this.energy = energy;
   }

   public List<Component> getTooltips() {
      return List.of(Component.literal(this.energy.getEnergyStored() + "/" + this.energy.getMaxEnergyStored() + " FE"));
   }

   @Override
   public void draw(GuiGraphics graphics) {
      int height = this.area.getHeight();
      int stored = (int)((float)height * ((float)this.energy.getEnergyStored() / (float)this.energy.getMaxEnergyStored()));
      graphics.fillGradient(
         this.area.getX(),
         this.area.getY() + (height - stored),
         this.area.getX() + this.area.getWidth(),
         this.area.getY() + this.area.getHeight(),
         -4909824,
         -10482944
      );
   }
}
