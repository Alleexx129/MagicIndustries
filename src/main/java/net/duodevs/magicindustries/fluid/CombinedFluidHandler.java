package net.duodevs.magicindustries.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CombinedFluidHandler implements IFluidHandler {
   private final IFluidHandler[] tanks;

   public CombinedFluidHandler(IFluidHandler... tanks) {
      this.tanks = tanks;
   }

   public int getTanks() {
      int count = 0;

      for (IFluidHandler tank : this.tanks) {
         count += tank.getTanks();
      }

      return count;
   }

   public FluidStack getFluidInTank(int tank) {
      for (IFluidHandler handler : this.tanks) {
         if (tank < handler.getTanks()) {
            return handler.getFluidInTank(tank);
         }

         tank -= handler.getTanks();
      }

      return FluidStack.EMPTY;
   }

   public int getTankCapacity(int tank) {
      for (IFluidHandler handler : this.tanks) {
         if (tank < handler.getTanks()) {
            return handler.getTankCapacity(tank);
         }

         tank -= handler.getTanks();
      }

      return 0;
   }

   public boolean isFluidValid(int tank, FluidStack stack) {
      for (IFluidHandler handler : this.tanks) {
         if (tank < handler.getTanks()) {
            return handler.isFluidValid(tank, stack);
         }

         tank -= handler.getTanks();
      }

      return false;
   }

   public int fill(FluidStack resource, FluidAction action) {
      for (IFluidHandler handler : this.tanks) {
         int filled = handler.fill(resource, action);
         if (filled > 0) {
            return filled;
         }
      }

      return 0;
   }

   public FluidStack drain(FluidStack resource, FluidAction action) {
      for (IFluidHandler handler : this.tanks) {
         FluidStack drained = handler.drain(resource, action);
         if (!drained.isEmpty()) {
            return drained;
         }
      }

      return FluidStack.EMPTY;
   }

   public FluidStack drain(int maxDrain, FluidAction action) {
      for (IFluidHandler handler : this.tanks) {
         FluidStack drained = handler.drain(maxDrain, action);
         if (!drained.isEmpty()) {
            return drained;
         }
      }

      return FluidStack.EMPTY;
   }
}
