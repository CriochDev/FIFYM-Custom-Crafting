package net.crioch.fifymcustomcrafting.interfaces;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.minecraft.recipe.book.RecipeBook;

public interface ComponentRecipeResultCollection {
    default void computeComponentCraftables(ComponentRecipeMatcher recipeFinder, int gridWidth, int gridHeight, RecipeBook recipeBook) {}
}
