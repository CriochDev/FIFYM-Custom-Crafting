package net.crioch.fifymcc.mixin.recipe;

import net.crioch.fifymcc.interfaces.IngredientAdditionalMethods;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Redirect(method = "isItemRecipeIngredient", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean useCleanTestForItemRecipeIngredient(Ingredient ingredient, ItemStack itemStack) {
        return ((IngredientAdditionalMethods)(Object)ingredient).fIFYM_CustomCrafting$testClean(itemStack);
    }

    @Redirect(method = "isPotionRecipeIngredient", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean useCleanTestForPotionRecipeIngredient(Ingredient ingredient, ItemStack itemStack) {
        return ((IngredientAdditionalMethods)(Object)ingredient).fIFYM_CustomCrafting$testClean(itemStack);
    }

    @Redirect(method = "hasItemRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean useCleanTestForHasItemRecipe(Ingredient ingredient, ItemStack itemStack) {
        return ((IngredientAdditionalMethods)(Object)ingredient).fIFYM_CustomCrafting$testClean(itemStack);
    }

    @Redirect(method = "hasPotionRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean useCleanTestForHasPotionRecipe(Ingredient ingredient, ItemStack itemStack) {
        return ((IngredientAdditionalMethods)(Object)ingredient).fIFYM_CustomCrafting$testClean(itemStack);
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean useCleanTestForCrafting(Ingredient ingredient, ItemStack itemStack) {
        return ((IngredientAdditionalMethods)(Object)ingredient).fIFYM_CustomCrafting$testClean(itemStack);
    }

    @Redirect(method = "isPotionType", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean useCleanTestForPotionCheck(Ingredient ingredient, ItemStack itemStack) {
        return ((IngredientAdditionalMethods)(Object)ingredient).fIFYM_CustomCrafting$testClean(itemStack);
    }
}
