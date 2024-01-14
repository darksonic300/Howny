package dev.rosyo.howny.common.entity;

import dev.rosyo.howny.common.registry.BlockEntityRegistry;
import dev.rosyo.howny.server.packets.HownyMessages;
import dev.rosyo.howny.server.packets.ItemStackSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class FloweringLogAltarEntity extends BlockEntity {
    public FloweringLogAltarEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.FLOWERING_LOG_ALTAR.get(), blockPos, blockState);
    }

    public final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                HownyMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }
    };

    public ItemStack getRenderStack() {
        return itemHandler.getStackInSlot(1);
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FloweringLogAltarEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }
    }
}

