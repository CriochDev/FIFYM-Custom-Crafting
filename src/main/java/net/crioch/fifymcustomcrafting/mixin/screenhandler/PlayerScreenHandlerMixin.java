package net.crioch.fifymcustomcrafting.mixin.screenhandler;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeInputProvider;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerScreenHandler.class)
public class PlayerScreenHandlerMixin implements ComponentRecipeScreenHandler<RecipeInputInventory> {
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
    public boolean matchesWithComponents(RecipeEntry<? extends Recipe<RecipeInputInventory>> recipe) {
        return recipe.value().matches(this.craftingInput, this.owner.getWorld());
    }
}
