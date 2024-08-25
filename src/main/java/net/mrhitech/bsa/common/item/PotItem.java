package net.mrhitech.bsa.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;

public class PotItem extends Item {

    public Block transformed_firepit;

    public PotItem(Properties properties, Block f_transformed_firepit) {
        super(properties);
        transformed_firepit = f_transformed_firepit;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
    {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        AbstractFirepitBlockEntity<?> firepit = level.getBlockEntity(pos, TFCBlockEntities.FIREPIT.get()).orElse(null);
        if (firepit != null && !(player != null && player.isShiftKeyDown()))
        {
            if (!level.isClientSide)
            {
                Block newBlock = transformed_firepit;
                BlockState state = level.getBlockState(pos);
                AbstractFirepitBlockEntity.convertTo(level, pos, state, firepit, newBlock);
                if (!(player != null && player.isCreative())) stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
