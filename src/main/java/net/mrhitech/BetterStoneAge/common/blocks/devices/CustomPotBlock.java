package net.mrhitech.BetterStoneAge.common.blocks.devices;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.TFCDamageSources;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.PotBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.PotBlock;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemHandlerHelper;
import net.mrhitech.BetterStoneAge.common.blockentities.BetterStoneAgeBlockEntities;
import net.mrhitech.BetterStoneAge.common.item.BetterStoneAgeItems;


import java.util.function.Supplier;


public class CustomPotBlock extends PotBlock {

    public Supplier<Item> stored_item;
    public Supplier<BlockEntityType> pot_block_entity_type_supplier;
    public DyeColor color;


    public CustomPotBlock(ExtendedProperties properties, Supplier<Item> f_stored_item, Supplier<BlockEntityType>f_pot_block_entity_type_supplier, DyeColor f_color) {
        super(properties);
        stored_item = f_stored_item;
        color = f_color;
        pot_block_entity_type_supplier = f_pot_block_entity_type_supplier;

    }
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        return (InteractionResult)level.getBlockEntity(pos, (BlockEntityType) BetterStoneAgeBlockEntities.GLAZED_POTS.get(color).get()).map((f_pot) -> {
            PotBlockEntity pot = (PotBlockEntity) f_pot;
            ItemStack stack = player.getItemInHand(hand);
            if (!pot.isBoiling() && stack.isEmpty() && player.isShiftKeyDown()) {
                if ((Boolean)state.getValue(LIT)) {
                    TFCDamageSources.pot(player, 1.0F);
                    Helpers.playSound(level, pos, (SoundEvent)TFCSounds.ITEM_COOL.get());
                } else {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack((ItemLike) BetterStoneAgeItems.GLAZED_POTS.get(color).get()));
                    AbstractFirepitBlockEntity.convertTo(level, pos, state, pot, (Block)TFCBlocks.FIREPIT.get());
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (!pot.isBoiling() && FluidHelpers.transferBetweenBlockEntityAndItem(stack, pot, player, hand)) {
                pot.markForSync();
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                if (!pot.isBoiling()) {
                    InteractionResult interactResult = pot.interactWithOutput(player, stack);
                    if (interactResult != InteractionResult.PASS) {
                        return interactResult;
                    }
                }

                if (tryInsertLog(player, stack, pot, result.getLocation().y - (double)pos.getY() < 0.6)) {
                    return InteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    if (player instanceof ServerPlayer) {
                        ServerPlayer serverPlayer = (ServerPlayer)player;
                        Helpers.openScreen(serverPlayer, pot, pos);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }).orElse(InteractionResult.PASS);
    }


//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
//        System.out.println("Right-clicking a " + color + "-colored pot");
//        return (InteractionResult) level.getBlockEntity(pos, (pot_block_entity_type_supplier).get()).map((f_pot) -> {
//            System.out.println("Currently getting interaction result");
//            PotBlockEntity pot = (PotBlockEntity)f_pot;
//            ItemStack stack = player.getItemInHand(hand);
//            if (!pot.isBoiling() && stack.isEmpty() && player.isShiftKeyDown()) {
//                System.out.println("Pot is not boiling, and stack is empty, and the shift key is down!");
//                if ((Boolean)state.getValue(LIT)) {
//                    System.out.println("This should burn the player");
//                    TFCDamageSources.pot(player, 1.0F);
//                    Helpers.playSound(level, pos, (SoundEvent) TFCSounds.ITEM_COOL.get());
//                } else {
//                    System.out.println("This should give the pot to the player");
//                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(stored_item.get()));
//                    AbstractFirepitBlockEntity.convertTo(level, pos, state, pot, (Block) TFCBlocks.FIREPIT.get());
//                }
//
//                return InteractionResult.sidedSuccess(level.isClientSide);
//            } else if (!pot.isBoiling() && FluidHelpers.transferBetweenBlockEntityAndItem(stack, pot, player, hand)) {
//                System.out.println("Giving fluids to the pot");
//                pot.markForSync();
//                return InteractionResult.sidedSuccess(level.isClientSide);
//            } else {
//                if (!pot.isBoiling()) {
//                    InteractionResult interactResult = pot.interactWithOutput(player, stack);
//                    if (interactResult != InteractionResult.PASS) {
//                        return interactResult;
//                    }
//                }
//
//                if (tryInsertLog(player, stack, pot, result.getLocation().y - (double)pos.getY() < 0.6)) {
//                    System.out.println("I think this is inserting fluid into the pot?");
//                    return InteractionResult.sidedSuccess(level.isClientSide);
//                } else {
//                    System.out.println("I'm me so I should open the pot menu");
//                    if (player instanceof ServerPlayer) {
//                        System.out.println("Opening the pot menu");
//                        ServerPlayer serverPlayer = (ServerPlayer)player;
//                        Helpers.openScreen(serverPlayer, pot, pos);
//                    }
//                    System.out.println("Returning!");
//                    return InteractionResult.sidedSuccess(level.isClientSide);
//                }
//            }
//        }).orElse(InteractionResult.PASS);
//    }

}
