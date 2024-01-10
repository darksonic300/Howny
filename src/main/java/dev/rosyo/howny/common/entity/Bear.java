package dev.rosyo.howny.common.entity;

import dev.rosyo.howny.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class Bear extends TamableAnimal implements GeoEntity, NeutralMob {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final float STAND_ANIMATION_TICKS = 6.0F;
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    private int warningSoundTicks;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;

    public Bear(EntityType<? extends Bear> entityType, Level level) {
        super(entityType, level);
    }

    public Bear(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(EntityRegistry.BEAR.get(), level);
    }

    @javax.annotation.Nullable
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        return EntityRegistry.BEAR.get().create(level);
    }

    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.HONEY_BOTTLE);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new Bear.BearMeleeAttackGoal());
        this.goalSelector.addGoal(1, new Bear.BearPanicGoal());
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(3, new Bear.BearHurtByTargetGoal());
        this.targetSelector.addGoal(4, new Bear.BearAttackPlayersGoal());
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Fox.class, 10, true, true, (Predicate<LivingEntity>)null));
        this.targetSelector.addGoal(7, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        if (this.isVehicle()) {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof HoneyGolem honeyGolem)
                return honeyGolem;
        }
        return null;
    }

    public static boolean checkBearSpawnRules(EntityType<Bear> p_218250_, LevelAccessor p_218251_, MobSpawnType p_218252_, BlockPos p_218253_, RandomSource p_218254_) {
        Holder<Biome> holder = p_218251_.getBiome(p_218253_);
        if (!holder.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)) {
            return checkAnimalSpawnRules(p_218250_, p_218251_, p_218252_, p_218253_, p_218254_);
        } else {
            return isBrightEnoughToSpawn(p_218251_, p_218253_) && p_218251_.getBlockState(p_218253_.below()).is(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readPersistentAngerSaveData(this.level(), compoundTag);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.addPersistentAngerSaveData(compoundTag);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public void setRemainingPersistentAngerTime(int i) {
        this.remainingPersistentAngerTime = i;
    }

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    public void setPersistentAngerTarget(@javax.annotation.Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    @javax.annotation.Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? SoundEvents.POLAR_BEAR_AMBIENT_BABY : SoundEvents.POLAR_BEAR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_29559_) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    protected void playStepSound(BlockPos p_29545_, BlockState p_29546_) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.playSound(SoundEvents.POLAR_BEAR_WARNING, 1.0F, this.getVoicePitch());
            this.warningSoundTicks = 40;
        }

    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STANDING_ID, false);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
                this.refreshDimensions();
            }

            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.isStanding()) {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            } else {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }
    }

    public EntityDimensions getDimensions(Pose pose) {
        if (this.clientSideStandAnimation > 0.0F) {
            float f = this.clientSideStandAnimation / 6.0F;
            float f1 = 1.0F + f;
            return super.getDimensions(pose).scale(1.0F, f1);
        } else {
            return super.getDimensions(pose);
        }
    }

    public boolean doHurtTarget(Entity entity) {
        boolean flag = entity.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this, entity);
        }

        return flag;
    }

    public boolean isStanding() {
        return this.entityData.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean b) {
        this.entityData.set(DATA_STANDING_ID, b);
    }

    public float getStandingAnimationScale(float p_29570_) {
        return Mth.lerp(p_29570_, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
    }

    protected float getWaterSlowDown() {
        return 0.98F;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, spawnGroupData, compoundTag);
    }


    class BearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
        public BearAttackPlayersGoal() {
            super(Bear.this, Player.class, 20, true, true, (Predicate<LivingEntity>)null);
        }

        public boolean canUse() {
            if (Bear.this.isBaby()) {
                return false;
            } else {
                if (super.canUse()) {
                    for(Bear bear : Bear.this.level().getEntitiesOfClass(Bear.class, Bear.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                        if (bear.isBaby() && !Bear.this.isVehicle()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }

    class BearHurtByTargetGoal extends HurtByTargetGoal {
        public BearHurtByTargetGoal() {
            super(Bear.this);
        }

        public void start() {
            super.start();
            if (Bear.this.isBaby()) {
                this.alertOthers();
                this.stop();
            }

        }

        protected void alertOther(Mob mob, LivingEntity p_29581_) {
            if (mob instanceof Bear && !mob.isBaby()) {
                super.alertOther(mob, p_29581_);
            }

        }
    }

    class BearMeleeAttackGoal extends MeleeAttackGoal {
        public BearMeleeAttackGoal() {
            super(Bear.this, 1.25D, true);
        }

        protected void checkAndPerformAttack(LivingEntity livingEntity, double distance) {
            double d0 = this.getAttackReachSqr(livingEntity);
            if (distance <= d0 && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(livingEntity);
                Bear.this.setStanding(false);
            } else if (distance <= d0 * 2.0D) {
                if (this.isTimeToAttack()) {
                    Bear.this.setStanding(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    Bear.this.setStanding(true);
                    Bear.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                Bear.this.setStanding(false);
            }

        }

        public void stop() {
            Bear.this.setStanding(false);
            super.stop();
        }

        protected double getAttackReachSqr(LivingEntity livingEntity) {
            return (double)(4.0F + livingEntity.getBbWidth());
        }
    }

    class BearPanicGoal extends PanicGoal {
        public BearPanicGoal() {
            super(Bear.this, 2.0D);
        }

        protected boolean shouldPanic() {
            return this.mob.getLastHurtByMob() != null && this.mob.isBaby() || this.mob.isOnFire();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
