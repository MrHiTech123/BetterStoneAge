/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.mrhitech.bsa.common.blocks;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum BetterStoneAgeGroundcoverBlockType
{
    OBSIDIAN(GroundcoverBlock.SMALL, BetterStoneAgeItems.OBSIDIAN);

    private final VoxelShape shape;
    @Nullable
    private final Supplier<? extends Item> vanillaItem; // The vanilla item this corresponds to

    BetterStoneAgeGroundcoverBlockType(VoxelShape shape)
    {
        this(shape, null);
    }

    BetterStoneAgeGroundcoverBlockType(VoxelShape shape, @Nullable Supplier<? extends Item> vanillaItem)
    {
        this.shape = shape;
        this.vanillaItem = vanillaItem;
    }

    public VoxelShape getShape()
    {
        return shape;
    }
    
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Nullable
    public Function<Block, BlockItem> createBlockItem()
    {
        return vanillaItem == null ? block -> new BlockItem(block, new Item.Properties()) : null;
    }

    @Nullable
    public Supplier<? extends Item> getVanillaItem()
    {
        return vanillaItem;
    }
}