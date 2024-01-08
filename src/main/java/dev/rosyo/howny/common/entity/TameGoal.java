package dev.rosyo.howny.common.entity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class TameGoal extends Goal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
    protected final HoneyGolem animal;
    private final Class<? extends Bear> partnerClass;
    protected final Level level;
    @Nullable
    protected Bear partner;
    private int tameTime;
    private final double speedModifier;

    public TameGoal(HoneyGolem animal, double speed) {
        this(animal, speed, Bear.class);
    }

    public TameGoal(HoneyGolem animal, double speed, Class<? extends Bear> partner) {
        this.animal = animal;
        this.level = animal.level();
        this.partnerClass = partner;
        this.speedModifier = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        this.partner = (Bear) this.getFreePartner();
        if(animal.isPassenger())
            return false;
        return this.partner != null;
    }

    public boolean canContinueToUse() {
        return this.partner.isAlive() && this.tameTime < 60;
    }

    public void stop() {
        this.partner = null;
        this.tameTime = 0;
    }

    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.tameTime;
        if (this.tameTime >= this.adjustedTickDelay(60) && this.animal.distanceToSqr(this.partner) < 9.0D) {
            if(RandomSource.create().nextInt(0,3) > 1)
                this.tame();
            else if(!partner.isVehicle())
                animal.hurt(animal.damageSources().mobAttack(partner), 10.0f);
        }

    }

    @Nullable
    private Animal getFreePartner() {
        List<? extends Animal> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0D));
        double d0 = Double.MAX_VALUE;
        Animal animal = null;

        for(Animal animal1 : list) {
            if (!animal1.isVehicle() && this.animal.distanceToSqr(animal1) < d0) {
                animal = animal1;
                d0 = this.animal.distanceToSqr(animal1);
            }
        }

        return animal;
    }

    protected void tame() {
        this.animal.startRiding(partner);

        try {
            partner.tame(animal.level().getPlayerByUUID(animal.getOwnerUUID()));
        }catch (NullPointerException ignored){
        }

        partner.getNavigation().stop();
        partner.level().broadcastEntityEvent(partner, (byte)7);
    }
}