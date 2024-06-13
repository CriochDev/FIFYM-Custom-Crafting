package net.crioch.fifymcc.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentRecipeInputProvider;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin implements ComponentRecipeInputProvider {
    @Shadow
    protected DefaultedList<ItemStack> inventory;

    @Unique
    private BlockEntity self = ((BlockEntity)(Object)this);

    @Override
    public void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addInput(itemStack);
        }
    }

    @Inject(method = "getFuelTime", at = @At("HEAD"), cancellable = true)
    private void getFuelTime(ItemStack fuel, CallbackInfoReturnable<Integer> cir) {
        Integer fuelValue = fuel.get(FIFYMDataComponentTypes.FUEL_VALUE);
        cir.setReturnValue(fuelValue != null ? fuelValue : 0);
    }

    @Inject(method = "canUseAsFuel", at = @At("HEAD"), cancellable = true)
    private static void canUseAsFuel(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stack.getComponents().contains(FIFYMDataComponentTypes.FUEL_VALUE));
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
    private static boolean disableRemainderBlock(ItemStack instance) {
        return false;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private static void changeToComponentRemainderOnFuelBurned(ItemStack stack, int amount, @Local(argsOnly = true) World world, @Local(argsOnly = true) AbstractFurnaceBlockEntity blockEntity) {

        Remainder remainder = stack.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
        if (remainder != null) {
            stack = remainder.getRemainder(stack, world);
        } else {
            stack.decrement(1);
            if (stack.isEmpty()) {
                stack = ItemStack.EMPTY;
            }
        }
        blockEntity.setStack(1, stack);
    }

    @Redirect(method = "craftRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private static void inputSlotUsesRemainderComponentOnComsume(ItemStack stack, int amount, @Local(argsOnly = true) DefaultedList<ItemStack> slots) {
        Remainder remainder = stack.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);

        if (remainder != null) {
            slots.set(0, remainder.getRemainder(stack));
        } else {
            stack.decrement(amount);
        }
    }
}
