package dev.rosyo.howny.common.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class TamedGolem extends AbstractGolem implements OwnableEntity {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamedGolem.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TamedGolem.class, EntityDataSerializers.OPTIONAL_UUID);

    protected TamedGolem(EntityType<? extends TamedGolem> entityType, Level level) {
        super(entityType, level);
        setTame(true);
        this.reassessTameGoals();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.getOwnerUUID() != null) {
            compoundTag.putUUID("Owner", this.getOwnerUUID());
        }
    }

    public void readAdditionalSaveData(CompoundTag p_21815_) {
        super.readAdditionalSaveData(p_21815_);
        UUID uuid;
        if (p_21815_.hasUUID("Owner")) {
            uuid = p_21815_.getUUID("Owner");
        } else {
            String s = p_21815_.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
                this.setTame(true);
            } catch (Throwable throwable) {
                this.setTame(false);
            }
        }
    }

    public boolean canBeLeashed(Player player) {
        return !this.isLeashed();
    }

    public void handleEntityEvent(byte p_21807_) {
        super.handleEntityEvent(p_21807_);
    }


    public boolean isTame() {
        return true;
    }

    public void setTame(boolean p_21836_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_21836_) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b0 | 4));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b0 & -5));
        }

        this.reassessTameGoals();
    }

    protected void reassessTameGoals() {
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNERUUID_ID).orElse((UUID)null);
    }

    public void setOwnerUUID(@Nullable UUID p_21817_) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(p_21817_));
    }

    public boolean canAttack(LivingEntity p_21822_) {
        return false;
    }

    public boolean isOwnedBy(LivingEntity livingEntity) {
        return livingEntity == this.getOwner();
    }

    public Team getTeam() {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (livingentity != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isAlliedTo(Entity p_21833_) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (p_21833_ == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isAlliedTo(p_21833_);
            }
        }

        return super.isAlliedTo(p_21833_);
    }

    public void tame(Player player) {
        this.setTame(true);
        this.setOwnerUUID(player.getUUID());
    }

    public void die(DamageSource p_21809_) {
        // FORGE: Super moved to top so that death message would be cancelled properly
        net.minecraft.network.chat.Component deathMessage = this.getCombatTracker().getDeathMessage();
        super.die(p_21809_);

        if (this.dead)
            if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
                this.getOwner().sendSystemMessage(deathMessage);
            }
    }
}
