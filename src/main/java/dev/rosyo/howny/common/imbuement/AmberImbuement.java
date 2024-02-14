package dev.rosyo.howny.common.imbuement;

import net.minecraft.world.entity.EquipmentSlot;

public class AmberImbuement extends Imbuement {
    protected AmberImbuement(EquipmentSlot[] equipmentSlots) {
        super(Type.AMBER, equipmentSlots);
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
