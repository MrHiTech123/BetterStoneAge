package net.mrhitech.bsa.mixin;


import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.blockentities.PotBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.FirepitBlock;
import net.dries007.tfc.common.blocks.devices.PotBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.util.mixininterface.IPotBlockEntitySaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PotBlock.class, remap = false)
public abstract class PotBlockMixin extends FirepitBlock implements IPotBlockEntitySaver {
    
    public PotBlockMixin(ExtendedProperties properties) {
        super(properties);
    }
    
    
    private RegistryObject<BlockEntityType<PotBlockEntity>> entity = null;
    
    @Override
    public void setEntity(RegistryObject<BlockEntityType<PotBlockEntity>> f_entity) {
        entity = f_entity;
    }
    
    
    public RegistryObject<BlockEntityType<PotBlockEntity>> getEntity() {
        return this.entity;
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
    
    
    
    
    
    
    
}
