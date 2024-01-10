package dev.rosyo.howny.common.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class RidingOwnerHurtByTargetGoal extends TargetGoal {
    private final HoneyGolem honeyGolem;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public RidingOwnerHurtByTargetGoal(HoneyGolem honeyGolem) {
        super(honeyGolem, false);
        this.honeyGolem = honeyGolem;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        if (this.honeyGolem.isTame() && this.honeyGolem.isPassenger()) {
            LivingEntity livingentity = this.honeyGolem.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.ownerLastHurtBy = livingentity.getLastHurtByMob();
                int i = livingentity.getLastHurtByMobTimestamp();
                return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        if(this.mob.getVehicle() instanceof Bear bear)
            bear.setTarget(this.ownerLastHurtBy);

        LivingEntity livingentity = this.honeyGolem.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}
