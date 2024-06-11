package net.crioch.fifymcc.mixin.recipe;

import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.interfaces.IngredientAdditionalMethods;
import net.crioch.fifymcc.recipe.FIFYMRecipeMatcherHelper;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import java.util.Optional;

@Mixin(Ingredient.class)
public class IngredientMixin implements IngredientAdditionalMethods {
	@Inject(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
	private void test(CallbackInfoReturnable<Boolean> info, @Local(ordinal = 0, argsOnly = true)ItemStack itemStack, @Local(ordinal = 1) ItemStack recipeStack) {
		ComponentChanges recipeChanges = recipeStack.getComponentChanges();
		ComponentChanges itemChanges = FIFYMRecipeMatcherHelper.filterWithBlacklist(itemStack);
		boolean result;
		Optional<? extends Remainder> remainder = itemChanges.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
		if (recipeChanges.isEmpty() && !itemChanges.isEmpty() && !(itemChanges.size() == 1 && remainder != null && remainder.isPresent())) {
			result = false;
		} else {
			result = itemChanges.entrySet().containsAll(recipeChanges.entrySet());
		}

		info.setReturnValue(result);
	}

	@Override
	public boolean fIFYM_CustomCrafting$testWithFilteredComponents(ItemStack stack) {
		ItemStack testStack = new ItemStack(stack.getRegistryEntry(), stack.getCount(), FIFYMRecipeMatcherHelper.filterWithBlacklist(stack, false));
		return this.test(testStack);
	}

	@Shadow
	public boolean test(ItemStack stack) { return false; }
}