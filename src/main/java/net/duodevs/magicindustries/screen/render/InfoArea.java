package net.duodevs.magicindustries.screen.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;

public abstract class InfoArea {
   protected final Rect2i area;

   protected InfoArea(Rect2i area) {
      this.area = area;
   }

   public abstract void draw(GuiGraphics var1);
}
