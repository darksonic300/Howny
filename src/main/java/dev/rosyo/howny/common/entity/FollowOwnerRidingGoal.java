package dev.rosyo.howny.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class FollowOwnerRidingGoal extends Goal {
    public static final int TELEPORT_WHEN_DISTANCE_IS = 12;
    private static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;
    private static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;
    private static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;
    private final HoneyGolem honeyGolem;
    private LivingEntity owner;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;
    private final boolean canFly;

    public FollowOwnerRidingGoal(HoneyGolem honeyGolem, double speedModifier, float startDist, float stopDist, boolean canFly) {
        this.honeyGolem = honeyGolem;
        this.level = honeyGolem.level();
        this.speedModifier = speedModifier;
        this.navigation = honeyGolem.getNavigation();
        this.startDistance = startDist;
        this.stopDistance = stopDist;
        this.canFly = canFly;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(honeyGolem.getNavigation() instanceof GroundPathNavigation) && !(honeyGolem.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean canUse() {
        LivingEntity livingentity = this.honeyGolem.getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else if (this.honeyGolem.distanceToSqr(livingentity) < (double)(this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    public boolean canContinueToUse() {
        if (this.navigation.isDone() || (this.honeyGolem.getVehicle() instanceof Bear bear && bear.getNavigation().isDone())) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else {
            return !(this.honeyGolem.distanceToSqr(this.owner) <= (double)(this.stopDistance * this.stopDistance));
        }
    }

    private boolean unableToMove() {
        return !this.honeyGolem.isPassenger() || this.honeyGolem.isLeashed() || (this.honeyGolem.getVehicle() instanceof Bear bear && bear.isLeashed());
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.honeyGolem.getPathfindingMalus(BlockPathTypes.WATER);
        this.honeyGolem.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public void stop() {
        this.owner = null;
        this.navigation.stop();
        if(this.honeyGolem.getVehicle() instanceof Bear bear) bear.getNavigation().stop();
        this.honeyGolem.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    public void tick() {
        this.honeyGolem.getLookControl().setLookAt(this.owner, 10.0F, (float)this.honeyGolem.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (this.honeyGolem.distanceToSqr(this.owner) >= 144.0D) {
                this.teleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
                if(this.honeyGolem.getVehicle() instanceof Bear bear) bear.getNavigation().moveTo(this.owner, this.speedModifier);
            }

        }
    }

    private void teleportToOwner() {
        BlockPos blockpos = this.owner.blockPosition();

        for(int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private boolean maybeTeleportTo(int p_25304_, int p_25305_, int p_25306_) {
        if (Math.abs((double)p_25304_ - this.owner.getX()) < 2.0D && Math.abs((double)p_25306_ - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(p_25304_, p_25305_, p_25306_))) {
            return false;
        } else {
            this.honeyGolem.moveTo((double)p_25304_ + 0.5D, (double)p_25305_, (double)p_25306_ + 0.5D, this.honeyGolem.getYRot(), this.honeyGolem.getXRot());
            this.navigation.stop();
            if(this.honeyGolem.getVehicle() instanceof Bear bear) {
                bear.moveTo((double)p_25304_ + 0.5D, (double)p_25305_, (double)p_25306_ + 0.5D, this.honeyGolem.getYRot(), this.honeyGolem.getXRot());
                bear.getNavigation().stop();
            }
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos p_25308_) {
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, p_25308_.mutable());
        if (blockpathtypes != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.level.getBlockState(p_25308_.below());
            if (!this.canFly && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = p_25308_.subtract(this.honeyGolem.blockPosition());
                return this.level.noCollision(this.honeyGolem, this.honeyGolem.getBoundingBox().move(blockpos));
            }
        }
    }

    private int randomIntInclusive(int p_25301_, int p_25302_) {
        return this.honeyGolem.getRandom().nextInt(p_25302_ - p_25301_ + 1) + p_25301_;
    }
}