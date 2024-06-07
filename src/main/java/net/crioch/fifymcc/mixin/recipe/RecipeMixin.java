package net.crioch.fifymcc.mixin.recipe;

import net.crioch.fifymcc.component.FIFYDataComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Recipe.class)
public interface RecipeMixin<C extends Inventory> {
    @Inject(method = "getRemainder", at = @At("HEAD"), cancellable = true)
    private <C extends Inventory> void replaceGetRecipeRemainder(C inventory, CallbackInfoReturnable<DefaultedList<ItemStack>> cir) {

        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

        for(int i = 0; i < defaultedList.size(); ++i) {
            ItemStack stack = inventory.getStack(i);
            ComponentMap components = stack.getComponents();
            if (components.contains(FIFYDataComponentTypes.RECIPE_REMAINDER)) {
                defaultedList.set(i, components.get(FIFYDataComponentTypes.RECIPE_REMAINDER).getRemainder(stack));
            }
        }

        cir.setReturnValue(defaultedList);
    }
}
