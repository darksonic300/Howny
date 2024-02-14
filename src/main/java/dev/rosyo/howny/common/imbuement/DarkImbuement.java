package dev.rosyo.howny.common.imbuement;

import net.minecraft.world.entity.EquipmentSlot;

public class DarkImbuement extends Imbuement {
    protected DarkImbuement(EquipmentSlot[] equipmentSlots) {
        super(Type.DARK, equipmentSlots);
    }

    @Override
    public Type getType() {
        return Type.WHITE;
    }

    @Override
    public int getCost(int cost) {
        return super.getCost(cost);
    }
}
