package net.duodevs.magicindustries.util;

import net.minecraftforge.energy.EnergyStorage;

public abstract class ModEnergyStorage extends EnergyStorage {
   public ModEnergyStorage(int capacity, int maxReceive, int maxExtract) {
      super(capacity, maxReceive, maxExtract);
   }

   public int extractEnergy(int maxExtract, boolean simulate) {
      int extractedEnergy = super.extractEnergy(maxExtract, simulate);
      if (extractedEnergy != 0) {
         this.onEnergyChanged();
      }

      return extractedEnergy;
   }

   public int receiveEnergy(int maxReceive, boolean simulate) {
      if (maxReceive == 0) {
         return 0;
      } else {
         int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
         if (receiveEnergy != 0) {
            this.onEnergyChanged();
         }

         return receiveEnergy;
      }
   }

   public int setEnergy(int energy) {
      this.energy = energy;
      return energy;
   }

   public abstract void onEnergyChanged();
}
