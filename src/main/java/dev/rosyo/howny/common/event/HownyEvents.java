package dev.rosyo.howny.common.event;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.entity.HoneyGolem;
import dev.rosyo.howny.common.registry.EntityRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Howny.MOD_ID)
public class HownyEvents {

    @Nullable
    private static BlockPattern honeyGolem;

    @SubscribeEvent
    public static void onBlockPlaceEvent(BlockEvent.EntityPlaceEvent event){
        if(event.getPlacedBlock().is(Blocks.CANDLE)){
            trySpawnGolem(event.getEntity().level(), event.getPos(), event.getEntity());
        }
    }

    private static BlockPattern getOrCreateHoneyGolemFull() {
        if (honeyGolem == null)
            honeyGolem = BlockPatternBuilder.start().aisle("^", "#").where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.CANDLE)))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.HONEYCOMB_BLOCK))).build();

        return honeyGolem;
    }

    private static void trySpawnGolem(Level level, BlockPos blockPos, Entity entity) {
        BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch = getOrCreateHoneyGolemFull().find(level, blockPos);
        if (blockpattern$blockpatternmatch != null) {
            HoneyGolem honeygolem = EntityRegistry.HONEY_GOLEM.get().create(level);
            if(entity instanceof Player player)
                honeygolem.tame(player);

            if (honeygolem != null) {
                spawnGolemInWorld(level, blockpattern$blockpatternmatch, honeygolem, blockpattern$blockpatternmatch.getBlock(0, 1, 0).getPos());
            }
        }
    }

    private static void spawnGolemInWorld(Level p_249110_, BlockPattern.BlockPatternMatch p_251293_, Entity p_251251_, BlockPos p_251189_) {
        clearPatternBlocks(p_249110_, p_251293_);
        p_251251_.moveTo((double)p_251189_.getX() + 0.5D, (double)p_251189_.getY() + 0.05D, (double)p_251189_.getZ() + 0.5D, 0.0F, 0.0F);
        p_249110_.addFreshEntity(p_251251_);

        for(ServerPlayer serverplayer : p_249110_.getEntitiesOfClass(ServerPlayer.class, p_251251_.getBoundingBox().inflate(5.0D))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, p_251251_);
        }

        updatePatternBlocks(p_249110_, p_251293_);
    }

    public static void clearPatternBlocks(Level p_249604_, BlockPattern.BlockPatternMatch p_251190_) {
        for(int i = 0; i < p_251190_.getWidth(); ++i) {
            for(int j = 0; j < p_251190_.getHeight(); ++j) {
                BlockInWorld blockinworld = p_251190_.getBlock(i, j, 0);
                p_249604_.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                p_249604_.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
            }
        }

    }

    public static void updatePatternBlocks(Level p_248711_, BlockPattern.BlockPatternMatch p_251935_) {
        for(int i = 0; i < p_251935_.getWidth(); ++i) {
            for(int j = 0; j < p_251935_.getHeight(); ++j) {
                BlockInWorld blockinworld = p_251935_.getBlock(i, j, 0);
                p_248711_.blockUpdated(blockinworld.getPos(), Blocks.AIR);
            }
        }

    }
}
