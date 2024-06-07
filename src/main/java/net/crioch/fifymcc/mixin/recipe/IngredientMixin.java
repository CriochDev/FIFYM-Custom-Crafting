package net.crioch.fifymcc.mixin.recipe;

import net.crioch.fifymcc.component.FIFYDataComponentTypes;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin(Ingredient.class)
public class IngredientMixin {
	@Inject(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
	private void test(CallbackInfoReturnable<Boolean> info, @Local(ordinal = 0, argsOnly = true)ItemStack itemStack, @Local(ordinal = 1) ItemStack recipeStack) {
		ComponentChanges recipeChanges = recipeStack.getComponentChanges();
		ComponentChanges itemChanges = itemStack.getComponentChanges();
		boolean result;
		if (recipeChanges.isEmpty() && (!itemChanges.isEmpty() || (itemChanges.size() == 1 && itemChanges.get(FIFYDataComponentTypes.RECIPE_REMAINDER).isPresent()))) {
			result = false;
		} else {
			result = itemChanges.entrySet().containsAll(recipeChanges.entrySet());
		}

		info.setReturnValue(result);
	}
}