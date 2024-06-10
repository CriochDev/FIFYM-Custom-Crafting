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

    @Redirect(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean useEnchantlessTestingWithinMatches(Ingredient instance, ItemStack itemStack) {
        return testFiltered(instance, itemStack);
    }

    @Redirect(method = "testTemplate", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean useEnchantlessTestingForTemplateIngredient(Ingredient instance, ItemStack itemStack) {
        return testFiltered(instance, itemStack);
    }

    @Redirect(method = "testAddition", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean useEnchantlessTestingForAdditionIngredient(Ingredient instance, ItemStack itemStack) {
        return testFiltered(instance, itemStack);
    }

    @Redirect(method = "testBase", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean useEnchantlessTestingForBaseIngredient(Ingredient instance, ItemStack itemStack) {
        return testFiltered(instance, itemStack);
    }

    private static boolean testFiltered(Ingredient ingredient, ItemStack stack) {
        return ((IngredientAdditionalMethods)(Object)ingredient).fIFYM_CustomCrafting$testWithFilteredComponents(stack);
    }
}
