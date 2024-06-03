package net.crioch.fifymcc.interfaces;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;

public interface ComponentRecipeScreenHandler<C extends Inventory> {
    default void populateComponentRecipeFinder(ComponentRecipeMatcher matcher) {
    }
    default boolean matchesWithComponents(RecipeEntry<? extends Recipe<C>> recipe) {
        return false;
    }
}
