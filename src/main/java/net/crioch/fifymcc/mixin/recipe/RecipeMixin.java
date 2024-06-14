package net.crioch.fifymcc.mixin.recipe;

import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.RecipeRemainderWithWorld;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.minecraft.component.ComponentMap;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RepairItemRecipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Recipe.class)
public interface RecipeMixin<C extends RecipeInput> extends RecipeRemainderWithWorld<C> {
    @Inject(method = "getRemainder", at = @At("HEAD"), cancellable = true)
    private void replaceGetRecipeRemainder(C input, CallbackInfoReturnable<DefaultedList<ItemStack>> cir) {

        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.getSize(), ItemStack.EMPTY);

        for(int i = 0; i < defaultedList.size(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
            Remainder remainder = stack.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
            if (remainder != null && !(this instanceof RepairItemRecipe)) {
                defaultedList.set(i, remainder.getRemainder(stack));
            }
        }

        cir.setReturnValue(defaultedList);
    }

    @Override
    default DefaultedList<ItemStack> fIFYM_CustomCrafting$getRemainder(C input, World world) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.getSize(), ItemStack.EMPTY);

        for(int i = 0; i < defaultedList.size(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
            Remainder remainder = stack.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
            if (remainder != null && !(this instanceof RepairItemRecipe)) {
                defaultedList.set(i, remainder.getRemainder(stack, world));
            }
        }

        return defaultedList;
    }
}
