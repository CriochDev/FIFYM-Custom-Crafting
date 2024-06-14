package net.crioch.fifymcc.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.component.remainder.StackRemainder;
import net.crioch.fifymcc.interfaces.BrewingStandBlockEntityFuel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin implements BrewingStandBlockEntityFuel {
    @Shadow
    int fuel;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 0, shift = At.Shift.BY, by = -5))
    private static void refuelBrewingStandWithComponent(World world, BlockPos pos, BlockState state, BrewingStandBlockEntity blockEntity, CallbackInfo ci) {
        ItemStack fuel = blockEntity.getStack(4);
        BrewingStandBlockEntityFuel getSetter = ((BrewingStandBlockEntityFuel)blockEntity);
        Remainder remainder = fuel.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
        int fuelValue = fuel.getOrDefault(FIFYMDataComponentTypes.BREWING_STAND_FUEL, 0);
        int adjustedFuelValue = getSetter.getFuel() + fuelValue;
        if (fuelValue != 0 && adjustedFuelValue < 21) {
            getSetter.setFuel(adjustedFuelValue);
            if (remainder != null) {
                blockEntity.setStack(4, remainder.getRemainder(fuel, world));
            } else {
                fuel.decrement(1);
            }

            blockEntity.markDirty();
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 0))
    private static boolean disableIfStatementRefuel(ItemStack instance, Item item) {
        return false;
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private static void checkandApplyRecipeRemainder(ItemStack stack, int amount, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos) {
        Remainder remainder = stack.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
        if (remainder != null) {
            ItemStack itemStack2 = remainder.getRemainder(stack);
            if (remainder instanceof StackRemainder) {
                if (!stack.isOf(itemStack2.getItem())) {
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), itemStack2);
                    stack.decrement(amount);
                }
            } else {
                stack = itemStack2;
            }
        } else {
            stack.decrement(amount);
        }
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private static Item preventHasRemainderBlockFromFiring(ItemStack stack) {
        return Items.AIR;
    }

    @Override
    public int getFuel() {
        return this.fuel;
    }

    @Override
    public void setFuel(int value) {
        this.fuel = value;
    }
}
