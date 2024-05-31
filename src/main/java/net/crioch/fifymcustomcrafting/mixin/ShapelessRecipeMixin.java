package net.crioch.fifymcustomcrafting.mixin;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.class)
public  class ShapelessRecipeMixin {

    @Unique
    ShapelessRecipe recipe = (ShapelessRecipe)((Object)this);

    @Final
    @Shadow
    DefaultedList<Ingredient> ingredients;

    @Inject(method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", at = @At("HEAD"), cancellable = true)
    private void matches(RecipeInputInventory recipeInputInventory, World world, CallbackInfoReturnable<Boolean> cir) {
        ComponentRecipeMatcher recipeMatcher = new ComponentRecipeMatcher();
        int i = 0;

        for(int j = 0; j < recipeInputInventory.size(); ++j) {
            ItemStack itemStack = recipeInputInventory.getStack(j);
            if (!itemStack.isEmpty()) {
                ++i;
                recipeMatcher.addInput(itemStack, 1);
            }
        }
        boolean correctSize = i == this.ingredients.size();
        boolean matches = recipeMatcher.match(this.recipe, null);
        cir.setReturnValue(correctSize && matches);
    }
}
