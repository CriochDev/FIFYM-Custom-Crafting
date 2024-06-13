package net.crioch.fifymcc.mixin.recipe.input;

import net.crioch.fifymcc.interfaces.ComponentCraftingRecipeInput;
import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.input.CraftingRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingRecipeInput.class)
public class CraftingRecipeInputMixin implements ComponentCraftingRecipeInput {
    @Unique
    private final ComponentRecipeMatcher recipeMatcher = new ComponentRecipeMatcher();

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;addInput(Lnet/minecraft/item/ItemStack;I)V"))
    private void addInputsToComponentRecipeMatcher(RecipeMatcher instance, ItemStack stack, int maxCount) {
        this.recipeMatcher.addInput(stack, maxCount);

    }

    @Override
    public ComponentRecipeMatcher fIFYM_CustomCrafting$getComponentRecipeMatcher() {
        return recipeMatcher;
    }
}
