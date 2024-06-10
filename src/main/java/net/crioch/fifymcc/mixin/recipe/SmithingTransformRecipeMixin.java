package net.crioch.fifymcc.mixin.recipe;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.crioch.fifymcc.interfaces.IngredientAdditionalMethods;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeMixin {

    @ModifyReturnValue(method = "matches", at = @At("RETURN"))
    private boolean checkReturnValues(boolean original) {
        return original;
    }
    @Redirect(method = "/(^test.+)|matches/", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean useEnchantlessTesting(Ingredient instance, ItemStack itemStack) {
        instance.getMatchingStacks();
        boolean result = ((IngredientAdditionalMethods)(Object)instance).fIFYM_CustomCrafting$testWithFilteredComponents(itemStack);
        return result;
    }
}
