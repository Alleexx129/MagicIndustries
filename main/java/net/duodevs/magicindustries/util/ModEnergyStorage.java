package net.duodevs.magicindustries.util;

import net.minecraftforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage {
    private final Runnable onChanged;

    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract, Runnable onChanged) {
        super(capacity, maxReceive, maxExtract);
        this.onChanged = onChanged == null ? () -> {} : onChanged;
    }

    public void setEnergy(int value) {
        int next = Math.max(0, Math.min(capacity, value));
        if (next != energy) {
            energy = next;
            onChanged.run();
        }
    }

    public void addEnergy(int value) {
        setEnergy(energy + value);
    }

    /** Used while loading NBT to avoid sending update packets before the tile is in a world. */
    public void setEnergySilently(int value) {
        energy = Math.max(0, Math.min(capacity, value));
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = super.extractEnergy(maxExtract, simulate);
        if (!simulate && extracted != 0) onChanged.run();
        return extracted;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && received != 0) onChanged.run();
        return received;
    }
}
