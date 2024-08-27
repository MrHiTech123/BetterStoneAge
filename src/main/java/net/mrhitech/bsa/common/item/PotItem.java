package net.mrhitech.bsa.common.item;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.PotBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.mrhitech.bsa.util.mixininterface.IPotColorSaver;

public class PotItem extends Item {

    public Block transformed_firepit;
    public DyeColor color;

    public PotItem(Properties properties, DyeColor f_color) {
        super(properties);
        color = f_color;
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
                Block newBlock = TFCBlocks.POT.get();
                BlockState state = level.getBlockState(pos);
                AbstractFirepitBlockEntity.convertTo(level, pos, state, firepit, newBlock);
                if (!(player != null && player.isCreative())) stack.shrink(1);
                ((IPotColorSaver)firepit.getBlockState().getBlock()).setColor(color);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
