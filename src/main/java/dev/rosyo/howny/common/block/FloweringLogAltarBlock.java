package dev.rosyo.howny.common.block;

import dev.rosyo.howny.common.entity.FloweringLogAltarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FloweringLogAltarBlock extends BaseEntityBlock {
    public FloweringLogAltarBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.MAIN_HAND && level.getBlockEntity(pos) instanceof FloweringLogAltarEntity altar) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.setMainHandItem(altar.handleInteraction(serverPlayer, serverPlayer.getMainHandItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FloweringLogAltarEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type
    ) {
        if (level.isClientSide) return null;
        return (entityLevel, entityState, entityType, entity) -> {
            if (entity instanceof FloweringLogAltarEntity altar) {
                altar.tick();
            }
        };
    }
}
