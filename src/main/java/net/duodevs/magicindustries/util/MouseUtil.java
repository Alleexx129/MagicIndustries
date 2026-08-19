package net.duodevs.magicindustries.util;

public class MouseUtil {
   public static boolean isMouseOver(double mouseX, double mouseY, int x, int y) {
      return isMouseOver(mouseX, mouseY, x, y, 16);
   }

   public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int size) {
      return isMouseOver(mouseX, mouseY, x, y, size, size);
   }

   public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
      return mouseX >= (double)x && mouseX <= (double)(x + sizeX) && mouseY >= (double)y && mouseY <= (double)(y + sizeY);
   }
}
