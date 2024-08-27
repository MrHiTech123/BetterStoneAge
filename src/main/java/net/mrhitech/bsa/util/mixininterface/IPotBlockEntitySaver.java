package net.mrhitech.bsa.util.mixininterface;

import net.dries007.tfc.common.blockentities.PotBlockEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public interface IPotBlockEntitySaver {
    void setColor(DyeColor f_color);
}
