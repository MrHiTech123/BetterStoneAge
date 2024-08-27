package net.mrhitech.bsa.mixin;


import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.FirepitBlock;
import net.dries007.tfc.common.blocks.devices.PotBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.mrhitech.bsa.util.mixininterface.IPotColorSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PotBlock.class, remap = false)
public abstract class PotBlockMixin extends FirepitBlock implements IPotColorSaver {
    
    public PotBlockMixin(ExtendedProperties properties) {
        super(properties);
    }
    
    
    private String color = null;
    private static String potColorKey = "bsa.pot_color";
    
    @Override
    public void setColor(DyeColor f_color) {
        color = (color == null)? f_color.getName() : color;
    }
    
    @Inject(method = "writeNbt", at = @At("HEAD"), remap = true)
    public void injectWriteNbt(CompoundTag nbt, CallbackInfo info) {
        if (color != null) {
            nbt.putString(potColorKey, color);
        }
    }
    
    @Inject(method = "readNbt", at = @At("HEAD"), remap = true)
    public void injectReadNbt(CompoundTag nbt, CallbackInfo info) {
        if (nbt.contains(potColorKey, 10)) {
            color = nbt.getString(potColorKey);
        }
    }
    
}
