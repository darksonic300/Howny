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
        super(honeyGolem, true);
        this.honeyGolem = honeyGolem;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        if (this.honeyGolem.isPassenger()) {
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
        LivingEntity target = this.ownerLastHurtBy;
        this.honeyGolem.setTarget(target);
        if(this.honeyGolem.getVehicle() instanceof Bear bear) {
            bear.setTarget(target);
            // Coordinate movement with bear
            this.honeyGolem.getNavigation().moveTo(bear, 1.0D);
            bear.getNavigation().moveTo(this.honeyGolem.getTarget(), 1.0D);
        }
        super.start();
    }
}
