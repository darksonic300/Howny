package dev.rosyo.howny.common.imbuement;

import net.minecraft.world.entity.EquipmentSlot;

public class WhiteImbuement extends Imbuement {
    protected WhiteImbuement(EquipmentSlot[] equipmentSlots) {
        super(Type.WHITE, equipmentSlots);
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
