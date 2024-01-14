package dev.rosyo.howny.common.entity;

import dev.rosyo.howny.common.registry.EntityRegistry;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class HoneyGolem extends TamedGolem implements GeoEntity {
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public HoneyGolem(EntityType<? extends TamedGolem> entityType, Level level) {
        super(entityType, level);
        setTame(true);
    }

    public HoneyGolem(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(EntityRegistry.HONEY_GOLEM.get(), level);
        setTame(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TameGoal(this, 0.9D, Bear.class));
        this.goalSelector.addGoal(2, new FollowOwnerRidingGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Bee.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(3, new RidingOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new RidingOwnerHurtTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    private PlayState predicate(AnimationState animationState) {

        if(animationState.isMoving()) {
            animationState.getController().setAnimation(RawAnimation.begin().thenPlay("animation.honey_golem.walk"));
            return PlayState.CONTINUE;
        }

        if(isPassenger()){
            animationState.getController().setAnimation(RawAnimation.begin().thenPlay("animation.honey_golem.sit"));
            return PlayState.CONTINUE;
        }

        animationState.getController().setAnimation(RawAnimation.begin().thenPlay("animation.honey_golem.idle"));
        return PlayState.CONTINUE;

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
