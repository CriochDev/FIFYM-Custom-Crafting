package net.crioch.fifymcc.interfaces;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.RecipeInput;

public interface ComponentRecipeScreenHandler<T extends RecipeInput> {
    default void populateComponentRecipeFinder(ComponentRecipeMatcher matcher) {
    }
    default boolean matchesWithComponents(RecipeEntry<? extends Recipe<T>> recipe) {
        return false;
    }
}
