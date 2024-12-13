package net.mrhitech.bsa.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class UnfiredDecoratedPotItem extends Item {
    public UnfiredDecoratedPotItem(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        DecoratedPotBlockEntity.Decorations decorations = DecoratedPotBlockEntity.Decorations.load(BlockItem.getBlockEntityData(stack));
        if (!decorations.equals(DecoratedPotBlockEntity.Decorations.EMPTY)) {
            tooltip.add(CommonComponents.EMPTY);
            Stream.of(decorations.front(), decorations.left(), decorations.right(), decorations.back()).forEach((decoration) -> {
                tooltip.add((new ItemStack(decoration, 1)).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY));
            });
        }
    }
}
