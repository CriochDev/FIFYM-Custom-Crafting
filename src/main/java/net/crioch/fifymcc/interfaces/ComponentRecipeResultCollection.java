package net.crioch.fifymcc.interfaces;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.minecraft.recipe.book.RecipeBook;

public interface ComponentRecipeResultCollection {
    default void computeComponentCraftables(ComponentRecipeMatcher recipeFinder, int gridWidth, int gridHeight, RecipeBook recipeBook) {}
}
