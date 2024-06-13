package net.crioch.fifymcc.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.IntList;
import net.crioch.fifymcc.interfaces.ComponentCraftingRecipeInput;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShapelessRecipe.class)
public  class ShapelessRecipeMixin {
    @Redirect(method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;match(Lnet/minecraft/recipe/Recipe;Lit/unimi/dsi/fastutil/ints/IntList;)Z"))
    private boolean matches(RecipeMatcher instance, Recipe<?> recipe, IntList output, @Local(argsOnly = true) CraftingRecipeInput crInput) {
        return ((ComponentCraftingRecipeInput)(Object)crInput).fIFYM_CustomCrafting$getComponentRecipeMatcher().match(recipe, null);
    }
}
