package net.duodevs.magicindustries.fluid;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class CombinedFluidHandler implements IFluidHandler {
    private final IFluidHandler[] tanks;

    public CombinedFluidHandler(IFluidHandler... tanks) {
        this.tanks = tanks;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        List<IFluidTankProperties> result = new ArrayList<>();
        for (IFluidHandler tank : tanks) {
            for (IFluidTankProperties prop : tank.getTankProperties()) {
                result.add(prop);
            }
        }
        return result.toArray(new IFluidTankProperties[0]);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null || resource.amount <= 0) return 0;
        for (IFluidHandler tank : tanks) {
            int accepted = tank.fill(resource, doFill);
            if (accepted > 0) return accepted;
        }
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource == null || resource.amount <= 0) return null;
        for (IFluidHandler tank : tanks) {
            FluidStack drained = tank.drain(resource, doDrain);
            if (drained != null && drained.amount > 0) return drained;
        }
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (maxDrain <= 0) return null;
        for (IFluidHandler tank : tanks) {
            FluidStack drained = tank.drain(maxDrain, doDrain);
            if (drained != null && drained.amount > 0) return drained;
        }
        return null;
    }
}
