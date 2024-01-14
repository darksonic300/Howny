package dev.rosyo.howny.common.entity;

import dev.rosyo.howny.common.registry.EntityRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class RidingOwnerHurtTargetGoal extends TargetGoal {
    private final HoneyGolem honeyGolem;
    private LivingEntity target;
    private int timestamp;

    public RidingOwnerHurtTargetGoal(HoneyGolem honeyGolem) {
        super(honeyGolem, false);
        this.honeyGolem = honeyGolem;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        if (this.honeyGolem.isAlive() && this.honeyGolem.isPassenger()) {
            LivingEntity livingentity = this.honeyGolem.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                target = livingentity.getLastHurtMob();
                int i = livingentity.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(target, TargetingConditions.DEFAULT);
            }
        } else {
            return false;
        }
    }

    public void start() {
        LivingEntity target = this.target;
        this.honeyGolem.setTarget(target);
        if(this.honeyGolem.getVehicle() instanceof Bear bear) {
            bear.setTarget(target);
            // Coordinate movement with bear
            this.honeyGolem.getNavigation().moveTo(target, 1.0D);
            bear.getNavigation().moveTo(target, 1.0D);
        }
        super.start();
    }
}