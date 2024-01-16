package dev.rosyo.howny.common.entity;

import dev.rosyo.howny.common.registry.BlockEntityRegistry;
import dev.rosyo.howny.server.packets.HownyMessages;
import dev.rosyo.howny.server.packets.ItemStackSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.ContainerSingleItem;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FloweringLogAltarEntity extends BlockEntity implements Clearable, ContainerSingleItem {
    private final NonNullList<ItemStack> items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private long tickCount;
    private int ticksSinceLastEvent;

    public FloweringLogAltarEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.FLOWERING_LOG_ALTAR.get(), blockPos, blockState);
    }

    public ItemStack getRenderStack(){
        return getFirstItem().isEmpty() ? new ItemStack(Items.AIR) : getFirstItem();
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("Item", 10)) {
            this.itemHandler.setStackInSlot(0, ItemStack.of(compoundTag.getCompound("Item")));
        }

        this.tickCount = compoundTag.getLong("TickCount");
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!this.getFirstItem().isEmpty()) {
            compoundTag.put("Item", this.getFirstItem().save(new CompoundTag()));
        }
        compoundTag.putLong("TickCount", this.tickCount);
    }


    public ItemStack getItem(int i) {
        return this.itemHandler.getStackInSlot(i);
    }

    public ItemStack removeItem(int i, int i1) {
        ItemStack itemstack = Objects.requireNonNullElse(this.itemHandler.getStackInSlot(i), ItemStack.EMPTY);
        this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);

        return itemstack;
    }

    public void setItem(int i, ItemStack itemStack) {
        if (this.level != null)
            this.itemHandler.setStackInSlot(i, itemStack);
    }

    public int getMaxStackSize() {
        return 1;
    }

    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return this.getItem(i).isEmpty();
    }

    public boolean canTakeItem(Container container, int i, ItemStack itemStack) {
        return container.hasAnyMatching(ItemStack::isEmpty);
    }

    public void popOutRecord() {
        if (this.level != null && !this.level.isClientSide) {
            BlockPos blockpos = this.getBlockPos();
            ItemStack itemstack = this.getFirstItem();
            if (!itemstack.isEmpty()) {
                this.clearContent();
                Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockpos, 0.5D, 1.01D, 0.5D).offsetRandom(this.level.random, 0.7F);
                ItemStack itemstack1 = itemstack.copy();
                ItemEntity itementity = new ItemEntity(this.level, vec3.x(), vec3.y(), vec3.z(), itemstack1);
                itementity.setDefaultPickUpDelay();
                this.level.addFreshEntity(itementity);
            }
        }
    }

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition,  new SimpleContainer(itemHandler.getStackInSlot(0)));
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        ++this.tickCount;
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}

