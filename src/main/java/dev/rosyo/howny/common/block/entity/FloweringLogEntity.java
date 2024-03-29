package dev.rosyo.howny.common.block.entity;

import dev.rosyo.howny.common.recipe.FloweringLogRecipe;
import dev.rosyo.howny.common.registry.BlockEntityRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import dev.rosyo.howny.common.screen.FloweringLogMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FloweringLogEntity extends BaseContainerBlockEntity implements Clearable, MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3);
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    public static final int FUEL_USES = 20;
    public static final int DATA_BREW_TIME = 0;
    public static final int DATA_FUEL_USES = 1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;
    int fuel;

    public FloweringLogEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.FLOWERING_LOG_ALTAR.get(), blockPos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> FloweringLogEntity.this.progress;
                    case 2 -> FloweringLogEntity.this.fuel;
                    case 1 -> FloweringLogEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> FloweringLogEntity.this.progress = pValue;
                    case 2 -> FloweringLogEntity.this.fuel = pValue;
                    case 1 -> FloweringLogEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.howny.flowering_log");
    }

    @Override
    protected Component getDefaultName() {
        return getDisplayName();
    }

    @Nullable
    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory) {
        return new FloweringLogMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("flowering_log.progress", progress);
        pTag.putByte("fuel", (byte)this.fuel);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("flowering_log.progress");
        this.fuel = pTag.getByte("fuel");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState, FloweringLogEntity logEntity) {
        ItemStack itemstack = itemHandler.getStackInSlot(1);
        if (logEntity.fuel <= 0 && itemstack.is(Items.HONEY_BOTTLE)) {
            logEntity.fuel = 20;
            setChanged(pLevel, pPos, pState);
        }

        if(hasRecipe() && logEntity.fuel > 0) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if(hasProgressFinished()) {
                itemstack.shrink(1);
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<FloweringLogRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        Optional<FloweringLogRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<FloweringLogRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        return this.level.getRecipeManager().getRecipeFor(FloweringLogRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    @Override
    public void clearContent() {

    }

    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        if (!this.itemHandler.getStackInSlot(0).isEmpty()) {
                return false;
        }
        if (!this.itemHandler.getStackInSlot(1).isEmpty()) {
            return false;
        }
        return this.itemHandler.getStackInSlot(2).isEmpty();
    }

    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < this.items.size() ? this.items.get(slot) : ItemStack.EMPTY;
    }

    public ItemStack removeItem(int p_58987_, int p_58988_) {
        return ContainerHelper.removeItem(this.items, p_58987_, p_58988_);
    }

    public ItemStack removeItemNoUpdate(int p_59015_) {
        return ContainerHelper.takeItem(this.items, p_59015_);
    }

    public void setItem(int p_58993_, ItemStack p_58994_) {
        if (p_58993_ >= 0 && p_58993_ < this.items.size()) {
            this.items.set(p_58993_, p_58994_);
        }

    }

    public boolean stillValid(Player p_59000_) {
        return Container.stillValidBlockEntity(this, p_59000_);
    }
}

