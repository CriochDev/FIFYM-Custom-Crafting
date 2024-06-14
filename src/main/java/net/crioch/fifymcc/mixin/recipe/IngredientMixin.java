package net.crioch.fifymcc.mixin.recipe;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.interfaces.IngredientAdditionalMethods;
import net.crioch.fifymcc.recipe.FIFYMHelper;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.sugar.Local;

import java.util.Optional;

@Mixin(Ingredient.class)
public class IngredientMixin implements IngredientAdditionalMethods {
	@ModifyReturnValue(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "RETURN", ordinal = 2))
	private boolean test(boolean isOfItem, @Local(ordinal = 0, argsOnly = true)ItemStack itemStack, @Local(ordinal = 1) ItemStack recipeStack) {
		ComponentChanges recipeChanges = recipeStack.getComponentChanges();
		ComponentChanges itemChanges = FIFYMHelper.filterWithBlacklist(itemStack);
		boolean result;
		Optional<? extends Remainder> remainder = itemChanges.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
		if (recipeChanges.isEmpty() && !itemChanges.isEmpty() && !(itemChanges.size() == 1 && remainder != null && remainder.isPresent())) {
			result = false;
		} else {
			result = itemChanges.entrySet().containsAll(recipeChanges.entrySet());
		}

		return result;
	}

	@Override
	public boolean fIFYM_CustomCrafting$testWithFilteredComponents(ItemStack stack) {
		ItemStack testStack = new ItemStack(stack.getRegistryEntry(), stack.getCount(), FIFYMHelper.filterWithBlacklist(stack, false));
		return this.test(testStack);
	}

	@Override
	public boolean fIFYM_CustomCrafting$testClean(ItemStack stack) {
		if (stack == null) {
			return false;
		} else if (this.isEmpty()) {
			return stack.isEmpty();
		} else {
			ItemStack[] var2 = this.getMatchingStacks();
			int var3 = var2.length;

			for(int var4 = 0; var4 < var3; ++var4) {
				ItemStack itemStack2 = var2[var4];
				if (itemStack2.isOf(stack.getItem())) {
					return true;
				}
			}

			return false;
		}
	}

	@Shadow
	public boolean test(ItemStack stack) { return false; }

	@Shadow
	boolean isEmpty() { return true; }

	@Shadow
	ItemStack[] getMatchingStacks() { return new ItemStack[0]; }
}