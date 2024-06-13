package net.crioch.fifymcc.mixin.screen;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentRecipeInputProvider;
import net.crioch.fifymcc.interfaces.ComponentRecipeScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerScreenHandler.class)
public class PlayerScreenHandlerMixin implements ComponentRecipeScreenHandler<CraftingRecipeInput> {
    @Final
    @Shadow
    private RecipeInputInventory craftingInput;

    @Final
    @Shadow
    private PlayerEntity owner;

    @Override
    public void populateComponentRecipeFinder(ComponentRecipeMatcher finder) {
        ((ComponentRecipeInputProvider)this.craftingInput).provideComponentRecipeInputs(finder);
    }

    @Override
    public boolean matchesWithComponents(RecipeEntry<? extends Recipe<CraftingRecipeInput>> recipe) {
        return recipe.value().matches(this.craftingInput.createRecipeInput(), this.owner.getWorld());
    }
}
