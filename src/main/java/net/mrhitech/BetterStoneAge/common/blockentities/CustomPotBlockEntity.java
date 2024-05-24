//package net.mrhitech.BetterStoneAge.common.blockentities;
//
//
//import net.dries007.tfc.common.TFCTags.Fluids;
//import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
//import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
//import net.dries007.tfc.common.blockentities.TFCBlockEntities;
//import net.dries007.tfc.common.blocks.devices.FirepitBlock;
//import net.dries007.tfc.common.capabilities.Capabilities;
//import net.dries007.tfc.common.capabilities.DelegateFluidHandler;
//import net.dries007.tfc.common.capabilities.DelegateItemHandler;
//import net.dries007.tfc.common.capabilities.InventoryItemHandler;
//import net.dries007.tfc.common.capabilities.PartialItemHandler;
//import net.dries007.tfc.common.capabilities.SidedHandler;
//import net.dries007.tfc.common.capabilities.heat.HeatCapability;
//import net.dries007.tfc.common.container.PotContainer;
//import net.dries007.tfc.common.recipes.PotRecipe;
//import net.dries007.tfc.common.recipes.TFCRecipeTypes;
//import net.dries007.tfc.common.recipes.PotRecipe.Output;
//import net.dries007.tfc.common.recipes.inventory.EmptyInventory;
//import net.dries007.tfc.config.TFCConfig;
//import net.dries007.tfc.util.Helpers;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.core.Direction.Plane;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.util.INBTSerializable;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import net.minecraftforge.fluids.capability.templates.FluidTank;
//import net.minecraftforge.items.IItemHandlerModifiable;
//import net.minecraftforge.items.ItemStackHandler;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import net.dries007.tfc.common.blockentities.PotBlockEntity.*;
//
//public class CustomPotBlockEntity extends AbstractFirepitBlockEntity<PotInventory> {
//    public static final int SLOT_EXTRA_INPUT_START = 4;
//    public static final int SLOT_EXTRA_INPUT_END = 8;
//    private static final Component NAME = Component.translatable("tfc.block_entity.pot");
//    private final SidedHandler.Builder<IFluidHandler> sidedFluidInventory;
//    @Nullable
//    private PotRecipe.@Nullable Output output = null;
//    private @Nullable PotRecipe cachedRecipe = null;
//    private int boilingTicks = 0;
//
//    public CustomPotBlockEntity(BlockPos pos, BlockState state) {
//        super((BlockEntityType) TFCBlockEntities.POT.get(), pos, state, net.dries007.tfc.common.blockentities.PotBlockEntity.PotInventory::new, NAME);
//        this.sidedFluidInventory = new SidedHandler.Builder((IFluidHandler)this.inventory);
//        if ((Boolean)TFCConfig.SERVER.firePitEnableAutomation.get()) {
//            this.sidedInventory.on((new PartialItemHandler(this.inventory)).insert(new int[]{3}).extract(new int[]{4, 5, 6, 7, 8}), Plane.HORIZONTAL).on((new PartialItemHandler(this.inventory)).insert(new int[]{4, 5, 6, 7, 8}), new Direction[]{Direction.UP});
//            this.sidedFluidInventory.on((IFluidHandler)this.inventory, Plane.HORIZONTAL);
//        }
//
//    }
//
//    public void loadAdditional(CompoundTag nbt) {
//        if (nbt.contains("output")) {
//            this.output = Output.read(nbt.getCompound("output"));
//        }
//
//        this.boilingTicks = nbt.getInt("boilingTicks");
//        super.loadAdditional(nbt);
//    }
//
//    public void saveAdditional(CompoundTag nbt) {
//        if (this.output != null) {
//            nbt.put("output", Output.write(this.output));
//        }
//
//        nbt.putInt("boilingTicks", this.boilingTicks);
//        super.saveAdditional(nbt);
//    }
//
//    public int getSlotStackLimit(int slot) {
//        return 1;
//    }
//
//    public boolean isItemValid(int slot, ItemStack stack) {
//        return slot >= 4 && slot <= 8 || super.isItemValid(slot, stack);
//    }
//
//    protected void handleCooking() {
//        if (this.isBoiling()) {
//            assert this.cachedRecipe != null;
//
//            if (this.boilingTicks < this.cachedRecipe.getDuration()) {
//                ++this.boilingTicks;
//                if (this.boilingTicks == 1) {
//                    this.markForSync();
//                }
//            } else {
//                PotRecipe recipe = this.cachedRecipe;
//                PotRecipe.Output output = recipe.getOutput((net.dries007.tfc.common.blockentities.PotBlockEntity.PotInventory)this.inventory);
//                ((net.dries007.tfc.common.blockentities.PotBlockEntity.PotInventory)this.inventory).tank.setFluid(FluidStack.EMPTY);
//
//                for(int slot = 4; slot <= 8; ++slot) {
//                    ((net.dries007.tfc.common.blockentities.PotBlockEntity.PotInventory)this.inventory).setStackInSlot(slot, ((net.dries007.tfc.common.blockentities.PotBlockEntity.PotInventory)this.inventory).getStackInSlot(slot).getCraftingRemainingItem());
//                }
//
//                output.onFinish((net.dries007.tfc.common.blockentities.PotBlockEntity.PotInventory)this.inventory);
//                if (!output.isEmpty()) {
//                    this.output = output;
//                }
//
//                this.cachedRecipe = null;
//                this.boilingTicks = 0;
//                this.updateCachedRecipe();
//                this.markForSync();
//            }
//        } else if (this.boilingTicks > 0) {
//            this.boilingTicks = 0;
//            this.markForSync();
//        }
//
//    }
//
//    public void onCalendarUpdate(long ticks) {
//        assert this.level != null;
//
//        if ((Boolean)this.level.getBlockState(this.worldPosition).getValue(FirepitBlock.LIT)) {
//            HeatCapability.Remainder remainder = HeatCapability.consumeFuelForTicks(ticks, this.inventory, this.burnTicks, this.burnTemperature, 0, 3);
//            this.burnTicks = remainder.burnTicks();
//            this.burnTemperature = remainder.burnTemperature();
//            this.needsSlotUpdate = true;
//            if (remainder.ticks() > 0L) {
//                if (this.isBoiling()) {
//                    assert this.cachedRecipe != null;
//
//                    long ticksUsedWhileBurning = ticks - remainder.ticks();
//                    if (ticksUsedWhileBurning > (long)(this.cachedRecipe.getDuration() - this.boilingTicks)) {
//                        this.boilingTicks = this.cachedRecipe.getDuration();
//                        this.handleCooking();
//                    }
//                }
//
//                this.extinguish(this.level.getBlockState(this.worldPosition));
//                this.coolInstantly();
//            } else if (this.isBoiling()) {
//                this.boilingTicks = (int)((long)this.boilingTicks + ticks);
//            }
//        }
//
//    }
//
//    protected void coolInstantly() {
//        this.boilingTicks = 0;
//        this.markForSync();
//    }
//
//    protected void updateCachedRecipe() {
//        assert this.level != null;
//
//        this.cachedRecipe = (PotRecipe)this.level.getRecipeManager().getRecipeFor((RecipeType)TFCRecipeTypes.POT.get(), (net.dries007.tfc.common.blockentities.PotBlockEntity.PotInventory)this.inventory, this.level).orElse((Object)null);
//    }
//
//    public boolean isBoiling() {
//        return this.cachedRecipe != null && this.output == null && this.cachedRecipe.isHotEnough(this.temperature);
//    }
//
//    public boolean shouldRenderAsBoiling() {
//        return this.boilingTicks > 0;
//    }
//
//    public InteractionResult interactWithOutput(Player player, ItemStack stack) {
//        if (this.output != null) {
//            InteractionResult result = this.output.onInteract(this, player, stack);
//            if (this.output.isEmpty()) {
//                this.output = null;
//            }
//
//            this.markForSync();
//            return result;
//        } else {
//            return InteractionResult.PASS;
//        }
//    }
//
//    @Nullable
//    public PotRecipe.@Nullable Output getOutput() {
//        return this.output;
//    }
//
//    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
//        return cap == Capabilities.FLUID ? this.sidedFluidInventory.getSidedHandler(side).cast() : super.getCapability(cap, side);
//    }
//
//    public @Nullable AbstractContainerMenu createMenu(int windowID, Inventory playerInv, Player player) {
//        return PotContainer.create(this, playerInv, windowID);
//    }
//
//    public static class PotInventory implements EmptyInventory, DelegateItemHandler, DelegateFluidHandler, INBTSerializable<CompoundTag> {
//        private final net.mrhitech.BetterStoneAge.common.blockentities.CustomPotBlockEntity pot;
//        private final ItemStackHandler inventory;
//        private final FluidTank tank;
//
//        public PotInventory(InventoryBlockEntity<net.dries007.tfc.common.blockentities.PotBlockEntity.PotInventory> entity) {
//            this.pot = (net.mrhitech.BetterStoneAge.common.blockentities.CustomPotBlockEntity)entity;
//            this.inventory = new InventoryItemHandler(entity, 9);
//            this.tank = new FluidTank(1000, (fluid) -> {
//                return Helpers.isFluid(fluid.getFluid(), Fluids.USABLE_IN_POT);
//            });
//        }
//
//        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
//            return this.pot.isBoiling() && slot >= 4 ? ItemStack.EMPTY : this.inventory.extractItem(slot, amount, simulate);
//        }
//
//        public IFluidHandler getFluidHandler() {
//            return this.tank;
//        }
//
//        public IItemHandlerModifiable getItemHandler() {
//            return this.inventory;
//        }
//
//        public CompoundTag serializeNBT() {
//            CompoundTag nbt = new CompoundTag();
//            nbt.put("inventory", this.inventory.serializeNBT());
//            nbt.put("tank", this.tank.writeToNBT(new CompoundTag()));
//            return nbt;
//        }
//
//        public void deserializeNBT(CompoundTag nbt) {
//            this.inventory.deserializeNBT(nbt.getCompound("inventory"));
//            this.tank.readFromNBT(nbt.getCompound("tank"));
//        }
//    }
//}
