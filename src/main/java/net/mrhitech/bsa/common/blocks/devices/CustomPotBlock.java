package net.mrhitech.bsa.common.blocks.devices;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.TFCDamageSources;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.PotBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.atomic.AtomicBoolean;

public class CustomPotBlock extends PotBlock {
    public final RegistryObject<Item> potReturnItem;
    public CustomPotBlock(ExtendedProperties properties, RegistryObject<Item> f_potReturnItem) {
        super(properties);
        potReturnItem = f_potReturnItem;
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        
        
        InteractionResult interactResult = level.getBlockEntity(pos, TFCBlockEntities.POT.get()).map((pot) -> {
            if (
                    (!pot.isBoiling() && stack.isEmpty() && player.isShiftKeyDown()) &&
                    !(!(Boolean)state.getValue(LIT) && !pot.isBoiling() && !(Boolean)state.getValue(LIT) && pot.getAsh() > 0)
            ) {
                if (state.getValue(LIT)) {
                    TFCDamageSources.pot(player, 1f);
                    Helpers.playSound(level, pos, TFCSounds.ITEM_COOL.get());
                }
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack((ItemLike) potReturnItem.get()));
                AbstractFirepitBlockEntity.convertTo(level, pos, state, pot, (Block) TFCBlocks.FIREPIT.get());
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else {
                return InteractionResult.PASS;
            }
        }).orElse(InteractionResult.PASS);
        
        if (interactResult == InteractionResult.PASS) {
            return super.use(state, level, pos, player, hand, result);
        }
        else {
            return interactResult;
        }
        
        
        
    }
}
