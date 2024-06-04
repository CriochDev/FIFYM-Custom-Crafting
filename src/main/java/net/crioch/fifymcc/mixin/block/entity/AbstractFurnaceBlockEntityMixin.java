package net.crioch.fifymcc.mixin.block.entity;

import net.crioch.fifymcc.components.FIFYDataComponentTypes;
import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentRecipeInputProvider;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin implements ComponentRecipeInputProvider {
    @Shadow
    protected DefaultedList<ItemStack> inventory;

    @Override
    public void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addInput(itemStack);
        }
    }

    @Inject(method = "getFuelTime", at = @At("HEAD"), cancellable = true)
    private void getFuelTime(ItemStack fuel, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(fuel.getComponents().getOrDefault(FIFYDataComponentTypes.FUEL_VALUE, 0));
    }

    @Inject(method = "canUseAsFuel", at = @At("HEAD"), cancellable = true)
    private static void canUseAsFuel(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stack.getComponents().contains(FIFYDataComponentTypes.FUEL_VALUE));
    }
}
