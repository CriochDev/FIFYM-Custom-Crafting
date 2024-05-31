package net.crioch.fifymcustomcrafting.mixin.screenhandler;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeInputProvider;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin implements ComponentRecipeScreenHandler<RecipeInputInventory> {
    @Final
    @Shadow
    private RecipeInputInventory input;

    @Final
    @Shadow
    private PlayerEntity player;

    @Override
    public void populateComponentRecipeFinder(ComponentRecipeMatcher finder) {
        ((ComponentRecipeInputProvider)this.input).provideComponentRecipeInputs(finder);
    }

    @Override
    public boolean matchesWithComponents(RecipeEntry<? extends Recipe<RecipeInputInventory>> recipe) {
        return recipe.value().matches(this.input, this.player.getWorld());
    }
}
