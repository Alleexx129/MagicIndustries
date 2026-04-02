package net.duodevs.magicindustries.screen.render;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

public class HelpInfoArea extends InfoArea {
   private final Component description;

   public HelpInfoArea(int xMin, int yMin, Component description) {
      this(xMin, yMin, description, 12, 18);
   }

   public HelpInfoArea(int xMin, int yMin, Component description, int width, int height) {
      super(new Rect2i(xMin, yMin, width, height));
      this.description = description;
   }

   public List<Component> getTooltips() {
      return List.of(this.description);
   }

   @Override
   public void draw(GuiGraphics graphics) {
   }
}
