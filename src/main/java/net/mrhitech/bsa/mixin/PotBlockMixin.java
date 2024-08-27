package net.mrhitech.bsa.mixin;


import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.TFCDamageSources;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.PotBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.FirepitBlock;
import net.dries007.tfc.common.blocks.devices.PotBlock;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.common.blockentities.BetterStoneAgeBlockEntities;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;
import net.mrhitech.bsa.util.mixininterface.IPotBlockEntitySaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PotBlock.class, remap = false)
public abstract class PotBlockMixin extends FirepitBlock implements IPotBlockEntitySaver {
    
    public PotBlockMixin(ExtendedProperties properties) {
        super(properties);
    }
    
    
    private DyeColor color = null;
    
    @Override
    public void setColor(DyeColor f_color) {
        color = f_color;
    }
    
    
    public DyeColor getColor() {
        return this.color;
    }
    
    public RegistryObject<BlockEntityType<PotBlockEntity>> getEntity() {
        return BetterStoneAgeBlockEntities.GLAZED_POTS.get(getColor());
    }
    
    public RegistryObject<Item> getItem() {
        return BetterStoneAgeItems.GLAZED_POTS.get(getColor());
    }
    
    @Inject(method = "animateTick", remap = true, at = @At(value = "RETURN"))
    public void animateTickMixin(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo info) {
        if (getEntity() == null) {
            return;
        }
        level.getBlockEntity(pos, getEntity().get()).ifPresent(pot -> {
            if (!pot.shouldRenderAsBoiling()) return;
            double x = pos.getX() + 0.5;
            double y = pos.getY();
            double z = pos.getZ() + 0.5;
            for (int i = 0; i < random.nextInt(5) + 4; i++)
            {
                level.addParticle(TFCParticles.BUBBLE.get(), false, x + random.nextFloat() * 0.375 - 0.1875, y + 0.625, z + random.nextFloat() * 0.375 - 0.1875, 0, 0.05D, 0);
            }
            level.addParticle(TFCParticles.STEAM.get(), false, x, y + 0.8, z, Helpers.triangle(random), 0.5, Helpers.triangle(random));
            level.playLocalSound(x, y, z, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.7F + 0.4F, false);
        });
    }
    
    // @Inject(method = "use", remap = true, at = @At(value = "HEAD"), cancellable = true)
    // public void useMixin(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> info) {
    //    
    //     if (player instanceof ServerPlayer) {
    //         System.out.println("Server");
    //     }
    //     else {
    //         System.out.println("Client");
    //     }
    //    
    //     if (getEntity() == null) {
    //         return;
    //     }
    //    
    //     System.out.println("Working here");
    //     System.out.println(level.getBlockEntity(pos, getEntity().get()));
    //    
    //     info.setReturnValue(
    //         level.getBlockEntity(pos, getEntity().get()).map(pot -> {
    //             final ItemStack stack = player.getItemInHand(hand);
    //             if (!pot.isBoiling() && stack.isEmpty() && player.isShiftKeyDown())
    //             {
    //                 if (state.getValue(LIT))
    //                 {
    //                     TFCDamageSources.pot(player, 1f);
    //                     Helpers.playSound(level, pos, TFCSounds.ITEM_COOL.get());
    //                     System.out.println("Pot is lit");
    //                 }
    //                 if (!state.getValue(LIT) && !pot.isBoiling() && !state.getValue(LIT) && pot.getAsh() > 0)
    //                 {
    //                     ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(TFCItems.POWDERS.get(Powder.WOOD_ASH).get(), pot.getAsh()));
    //                     pot.setAsh(0);
    //                     Helpers.playSound(level, pos, SoundEvents.SAND_BREAK);
    //                     return InteractionResult.sidedSuccess(level.isClientSide);
    //                 }
    //                 else
    //                 {
    //                     ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(getItem().get()));
    //                     AbstractFirepitBlockEntity.convertTo(level, pos, state, pot, TFCBlocks.FIREPIT.get());
    //                 }
    //                 return InteractionResult.sidedSuccess(level.isClientSide);
    //             }
    //             else if (!pot.isBoiling() && FluidHelpers.transferBetweenBlockEntityAndItem(stack, pot, player, hand))
    //             {
    //                 pot.setAndUpdateSlots(-1);
    //                 pot.markForSync();
    //                 return InteractionResult.sidedSuccess(level.isClientSide);
    //             }
    //             else
    //             {
    //                 if (!pot.isBoiling())
    //                 {
    //                     final InteractionResult interactResult = pot.interactWithOutput(player, stack);
    //                     if (interactResult != InteractionResult.PASS)
    //                     {
    //                         return interactResult;
    //                     }
    //                 }
    //                 if (tryInsertLog(player, stack, pot, result.getLocation().y - pos.getY() < 0.6))
    //                 {
    //                     return InteractionResult.sidedSuccess(level.isClientSide);
    //                 }
    //                 if (player instanceof ServerPlayer serverPlayer)
    //                 {
    //                     Helpers.openScreen(serverPlayer, pot, pos);
    //                 }
    //                 return InteractionResult.sidedSuccess(level.isClientSide);
    //             }
    //         }).orElse(InteractionResult.PASS)
    //     );
    //     // info.cancel();
    //    
    // }
    
    
    
    
    
}
