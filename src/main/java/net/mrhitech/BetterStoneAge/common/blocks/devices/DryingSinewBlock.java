package net.mrhitech.BetterStoneAge.common.blocks.devices;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.devices.BottomSupportedDeviceBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import net.mrhitech.BetterStoneAge.common.blocks.BetterStoneAgeBlockStateProperties;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.logging.Logger;


public class DryingSinewBlock extends BottomSupportedDeviceBlock {

    public BooleanProperty DRIED = BetterStoneAgeBlockStateProperties.DRIED;
    public static final VoxelShape SHAPE = box(0D, 0D, 0D, 16.0D, 1.0D, 16.0D);
    private final Supplier<? extends Item> dryItem;

    public DryingSinewBlock(ExtendedProperties properties, Supplier<? extends Item> f_dryItem) {
        super(properties, InventoryRemoveBehavior.NOOP, SHAPE);

        dryItem = f_dryItem;
        registerDefaultState(getStateDefinition().any().setValue(DRIED, false));

    }

    public @Nullable BlockState getBlockStateForPlacement(BlockPlaceContext context) {
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final BlockState state = level.getBlockState(pos);

        if (!state.getFluidState().isEmpty()) {
            return null;
        }

        return super.getStateForPlacement(context);

    }


    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return state.getValue(DRIED) ? dryItem.get().getDefaultInstance() : super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.isSecondaryUseActive() && Helpers.isItem(context.getItemInHand(), this.asItem()) && !state.getValue(DRIED) || super.canBeReplaced(state, context);
    }


    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TickCounterBlockEntity.reset(level, pos);
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(DRIED);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (state.getValue(DRIED)) return;
        level.getBlockEntity(pos, TFCBlockEntities.TICK_COUNTER.get()).ifPresent(counter -> {
            if (level.isRainingAt(pos)) {
                counter.resetCounter();
            }
            else {
                final int ticks = 48000;
                if (ticks > -1 && counter.getTicksSinceUpdate() > ticks) {
                    level.setBlockAndUpdate(pos, state.setValue(DRIED, true));
                    final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
                    for (Direction d : Direction.Plane.HORIZONTAL) {
                        cursor.setWithOffset(pos, d);
                        final BlockState stateAt = level.getBlockState(cursor);
                        if (stateAt.getBlock() instanceof DryingSinewBlock) {
                            level.scheduleTick(cursor, stateAt.getBlock(), 1);
                        }
                    }
                }
            }
        });

    }
    

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {


        DRIED = BetterStoneAgeBlockStateProperties.DRIED;

        super.createBlockStateDefinition(builder.add(this.DRIED));
    }



}
