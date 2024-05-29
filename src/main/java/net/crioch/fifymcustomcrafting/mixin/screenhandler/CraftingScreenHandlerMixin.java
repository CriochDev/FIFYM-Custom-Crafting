package net.crioch.fifymcustomcrafting.mixin.screenhandler;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeInputProvider;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeScreenHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin implements ComponentRecipeScreenHandler {
    @Final
    @Shadow
    private RecipeInputInventory input;

    @Override
    public void populateComponentRecipeFinder(ComponentRecipeMatcher finder) {
        ((ComponentRecipeInputProvider)this.input).provideComponentRecipeInputs(finder);
    }

    @Inject(method = "matches", at = @At("HEAD"))
    private void isCalled(RecipeEntry<? extends Recipe<RecipeInputInventory>> recipe, CallbackInfoReturnable<Boolean> cir) {
    }

    @ModifyReturnValue(method = "matches", at = @At(value = "RETURN"))
    public boolean detect(boolean original, @Local(argsOnly = true) RecipeEntry<? extends Recipe<RecipeInputInventory>> recipe) {
        return original;
    }
}
